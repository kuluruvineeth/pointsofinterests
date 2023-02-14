package com.kuluruvineeth.pointsofinterests.features.search.vm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.domain.features.poi.interactor.SearchPoiUseCase
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.PoiListItem
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.toListUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject


@HiltViewModel
class SearchVm @Inject constructor(
    private val searchPoiUseCase: SearchPoiUseCase
): ViewModel(){

    private val queryState = MutableStateFlow("")

    @RequiresApi(Build.VERSION_CODES.O)
    val searchScreenState = queryState
        .filter { it.isNotEmpty() }
        .debounce(500)
        .distinctUntilChanged()
        .map { query -> searchPoiUseCase(SearchPoiUseCase.Params(query)) }
        .map{result ->
            if(result!!.isEmpty()) SearchScreenUiState.NothingFound
            else SearchScreenUiState.SearchResult(result!!.map{it.toListUiModel()})
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            SearchScreenUiState.NothingFound
        )

    fun onSearch(query: String){
        queryState.value = query
    }
}

sealed class SearchScreenUiState{
    object NothingFound : SearchScreenUiState()
    data class SearchResult(val result : List<PoiListItem>) : SearchScreenUiState()
}