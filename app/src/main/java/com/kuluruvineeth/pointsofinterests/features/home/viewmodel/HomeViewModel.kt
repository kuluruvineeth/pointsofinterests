package com.kuluruvineeth.pointsofinterests.features.home.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.domain.features.categories.interactor.GetCategoriesByIdsUseCase
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.domain.features.poi.interactor.GetPoiListUseCase
import com.kuluruvineeth.domain.features.poi.interactor.GetUsedCategoriesUseCase
import com.kuluruvineeth.pointsofinterests.core.utils.ErrorDisplayObject
import com.kuluruvineeth.pointsofinterests.core.utils.RetryTrigger
import com.kuluruvineeth.pointsofinterests.core.utils.retryableFlow
import com.kuluruvineeth.pointsofinterests.core.utils.toDisplayObject
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.toUiModel
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.PoiListItem
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.PoiSortByUiOption
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.toDomain
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.toListUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    getUsedCategoriesUseCase: GetUsedCategoriesUseCase,
    private val getCategoriesUseCase: GetCategoriesByIdsUseCase,
    private val getPoiListUseCase: GetPoiListUseCase
) : ViewModel(){

    val categoriesState = getUsedCategoriesUseCase(Unit).flatMapConcat{ids ->
        getCategoriesUseCase(GetCategoriesByIdsUseCase.Params(ids!!))
            .map{ list -> list!!.map{it.toUiModel()}}
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _homeUiContentState = MutableStateFlow<HomeUiContentState>(HomeUiContentState.Loading)
    val homeUiContentState = _homeUiContentState.asStateFlow()

    private val _displaySortOptionUiState =
        MutableStateFlow(PoiSortByUiOption.DATE)
    val displaySortOptionUiState = _displaySortOptionUiState.asStateFlow()



    private val retryTrigger by lazy { RetryTrigger() }


    @RequiresApi(Build.VERSION_CODES.O)
    private val mainPoiState =
        savedStateHandle.getStateFlow(KEY_SORT_OPTION, PoiSortByUiOption.DATE)
            .onEach { _displaySortOptionUiState.value = it }
            .flatMapLatest { sortOption ->
                retryableFlow(retryTrigger){
                    getPoiListUseCase(GetPoiListUseCase.Params(sortOption.toDomain()))
                        .map { list -> list!!.map { it!!.toListUiModel() } }
                        .onEach {
                            if(it.isEmpty()) _homeUiContentState.value = HomeUiContentState.Empty
                            else _homeUiContentState.value = HomeUiContentState.Result(it)
                        }
                        .catch { _homeUiContentState.value = HomeUiContentState.Error(it.toDisplayObject()) }
                        .onStart { _homeUiContentState.value = HomeUiContentState.Loading }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PoiSortByUiOption.DATE
            )

    init {
        mainPoiState.launchIn(viewModelScope)
    }

    fun onRetry(){
        retryTrigger.retry()
    }

    fun onApplySortBy(option: PoiSortByUiOption){
        savedStateHandle[KEY_SORT_OPTION] = option
    }
    sealed class HomeUiContentState{
        object Loading : HomeUiContentState()
        object Empty : HomeUiContentState()
        data class Result(val poiList: List<PoiListItem>) : HomeUiContentState()
        data class Error(val displayObject: ErrorDisplayObject) : HomeUiContentState()
    }

    companion object{
        private const val KEY_SORT_OPTION = "sort_option"
    }
}
