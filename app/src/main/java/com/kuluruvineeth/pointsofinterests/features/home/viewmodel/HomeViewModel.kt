package com.kuluruvineeth.pointsofinterests.features.home.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.domain.features.categories.interactor.GetCategoriesByIdsUseCase
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.domain.features.poi.interactor.GetPoiListUseCase
import com.kuluruvineeth.domain.features.poi.interactor.GetUsedCategoriesUseCase
import com.kuluruvineeth.pointsofinterests.core.utils.RetryTrigger
import com.kuluruvineeth.pointsofinterests.core.utils.retryableFlow
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.toUiModel
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.PoiListItem
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.PoiSortByUiOption
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.toListUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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

    private val retryTrigger by lazy { RetryTrigger() }


    @RequiresApi(Build.VERSION_CODES.O)
    val homeUiContentState = retryableFlow(retryTrigger){
        getPoiListUseCase(null)
            .map { list -> list!!.map { it!!.toListUiModel() } }
            .map {
                if(it.isEmpty()) HomeUiContentState.Empty
                else HomeUiContentState.Result(it)
            }
            .catch{
                emit(HomeUiContentState.Error(it.message ?: "null"))
            }
            .onStart{emit(HomeUiContentState.Loading)}
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiContentState.Loading
        )

    fun onRetry(){
        retryTrigger.retry()
    }

    fun onSearch(query: String){

    }

    fun onApplySortBy(option: PoiSortByUiOption){

    }
    sealed class HomeUiContentState{
        object Loading : HomeUiContentState()
        object Empty : HomeUiContentState()
        data class Result(val poiList: List<PoiListItem>) : HomeUiContentState()
        data class Error(val message: String) : HomeUiContentState()
    }
}
