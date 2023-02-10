package com.kuluruvineeth.pointsofinterests.features.poi.create.viewmodel

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.kuluruvineeth.domain.features.categories.interactor.GetCategoriesUseCase
import com.kuluruvineeth.domain.features.poi.interactor.CreatePoiUseCase
import com.kuluruvineeth.domain.features.poi.interactor.GetWizardSuggestionUseCase
import com.kuluruvineeth.pointsofinterests.core.utils.ErrorDisplayObject
import com.kuluruvineeth.pointsofinterests.core.utils.toDisplayObject
import com.kuluruvineeth.pointsofinterests.features.poi.create.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CreatePoiViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getWizardSuggestionUseCase: GetWizardSuggestionUseCase,
    private val createPoiUseCase: CreatePoiUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel(){

    val screenState = MutableStateFlow<CreatePoiScreenState>(CreatePoiScreenState.Loading)
    val wizardSuggestionState = MutableStateFlow<WizardSuggestionUiState>(WizardSuggestionUiState.None)

    private val queryState = MutableStateFlow("")

    val searchState = queryState
        .onEach { if(it.isEmpty()) wizardSuggestionState.value =
            WizardSuggestionUiState.None
        }
        .filter { it.isNotEmpty() }
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapConcat { url ->
            channelFlow {
                send(getWizardSuggestionUseCase(GetWizardSuggestionUseCase.Params(url)))
            }.onStart {
                wizardSuggestionState.value = WizardSuggestionUiState.Loading
            }.catch { wizardSuggestionState.value =
                WizardSuggestionUiState.Error(it.toDisplayObject())
            }
        }.onEach {
            wizardSuggestionState.value =
                WizardSuggestionUiState.Success(it!!.toUiModel())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    val sharedContentState =
        savedStateHandle.getStateFlow(NavController.KEY_DEEP_LINK_INTENT,Intent())
            .map { intent -> intent.fetchSharedContent() }
            .onEach {
                if(
                    it.contentType == ContentType.MANUAL ||
                    it.contentType == ContentType.URL
                ){
                    screenState.value = CreatePoiScreenState.Wizard(it)
                }else{
                    screenState.value = CreatePoiScreenState.Form(it.toWizardSuggestion())
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SharedContent.EMPTY
            )

    fun onApplyWizardSuggestion(){
        (wizardSuggestionState.value as? WizardSuggestionUiState.Success)?.wizardSuggestionUiModel?.let { suggestionModel ->
            screenState.value = CreatePoiScreenState.Form(suggestionModel)
        }
    }

    fun onSkip(){
        val sharedContent = (screenState.value as CreatePoiScreenState.Wizard).sharedContent
        screenState.value = CreatePoiScreenState.Form(
            WizardSuggestionUiModel(
            url = sharedContent.content
        )
        )
    }

    fun onSave(){

    }

    fun onFetchWizardSuggestion(url: String){
        queryState.value = url
    }

    private fun SharedContent.toWizardSuggestion() = when(this.contentType){
        ContentType.LOCAL_IMAGE -> WizardSuggestionUiModel(imageUrl = content)
        ContentType.PLAIN_TEXT -> WizardSuggestionUiModel(body = content)
        else -> WizardSuggestionUiModel.EMPTY
    }
}

sealed class CreatePoiScreenState{
    data class Wizard(val sharedContent: SharedContent) : CreatePoiScreenState()
    data class Form(val suggestion: WizardSuggestionUiModel) : CreatePoiScreenState()
    object Loading: CreatePoiScreenState()
}

sealed class WizardSuggestionUiState{
    data class Success(val wizardSuggestionUiModel: WizardSuggestionUiModel) : WizardSuggestionUiState()
    object Loading : WizardSuggestionUiState()
    object None : WizardSuggestionUiState()
    data class Error(val displayObject: ErrorDisplayObject) : WizardSuggestionUiState()
}