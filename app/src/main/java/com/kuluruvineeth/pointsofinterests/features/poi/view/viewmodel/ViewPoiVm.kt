package com.kuluruvineeth.pointsofinterests.features.poi.view.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.domain.features.poi.interactor.*
import com.kuluruvineeth.domain.features.poi.models.PoiCommentPayload
import com.kuluruvineeth.pointsofinterests.features.poi.view.models.PoiDetailListItem
import com.kuluruvineeth.pointsofinterests.features.poi.view.models.toUIModelWithComments
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ViewPoiVm @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDetailedPoiUseCase: GetDetailedPoiUseCase,
    private val deletePoiUseCase: DeletePoiUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val addCommentsUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase
) : ViewModel(){

    private val _idState = MutableStateFlow("")
    val idState = _idState.asStateFlow()

    private val _finishScreenState = MutableStateFlow(false)
    val finishScreenState = _finishScreenState.asStateFlow()

    private val _uiState = MutableStateFlow<List<PoiDetailListItem>>(emptyList())
    val uiState = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    private val mainState =
        savedStateHandle.getStateFlow(Screen.ViewPoiDetailed.ARG_POI_ID,"")
            .filter { it.isNotEmpty() }
            .onEach { id -> _idState.value = id }
            .map { getDetailedPoiUseCase(GetDetailedPoiUseCase.Params(it)) }
            .flatMapConcat { poi ->
                getCommentsUseCase(GetCommentsUseCase.Params(poi!!.id))
                    .onEach { comments -> _uiState.value = poi.toUIModelWithComments(comments!!) }
                    .catch { error -> Log.e(ViewPoiVm::class.java.simpleName,error.message,error) }
            }

    init {
        mainState.launchIn(viewModelScope)
    }

    fun onAddComment(message: String){
        viewModelScope.launch {
            val payload = PoiCommentPayload(message)
            addCommentsUseCase(AddCommentUseCase.Params(idState.value,payload))
        }
    }

    fun onDeleteComment(id: String){
        viewModelScope.launch {
            deleteCommentUseCase(DeleteCommentUseCase.Params(id))
        }
    }

    fun onDeletePoi(){
        viewModelScope.launch {
            val id = _idState.value
            deletePoiUseCase(DeletePoiUseCase.Params(id))
            _finishScreenState.update { true }
        }
    }
}