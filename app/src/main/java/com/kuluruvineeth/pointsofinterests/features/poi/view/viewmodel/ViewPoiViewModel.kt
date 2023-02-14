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
class ViewPoiViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDetailedPoiUseCase: GetDetailedPoiUseCase,
    private val deletePoiUseCase: DeletePoiUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val addCommentsUseCase: AddCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase
) : ViewModel(){

    private val idState = MutableStateFlow("")


    private val _finishScreenState = MutableSharedFlow<Boolean>()
    val finishScreenState = _finishScreenState.asSharedFlow()

    private val _uiState = MutableStateFlow<List<PoiDetailListItem>>(emptyList())
    val uiState = _uiState.asStateFlow()

    private val _itemToDeleteState = MutableStateFlow<List<String>>(emptyList())
    val itemToDeleteState = _itemToDeleteState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    private val mainState =
        savedStateHandle.getStateFlow(Screen.ViewPoiDetailed.ARG_POI_ID,"")
            .filter { it.isNotEmpty() }
            .onEach { id -> idState.value = id }
            .map { getDetailedPoiUseCase(GetDetailedPoiUseCase.Params(it)) }
            .flatMapConcat { poi ->
                getCommentsUseCase(GetCommentsUseCase.Params(poi!!.id))
                    .onEach { comments -> _uiState.value = poi.toUIModelWithComments(comments!!) }
                    .catch { error -> Log.e(ViewPoiViewModel::class.java.simpleName,error.message,error) }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ""
            )

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
        val updatedList = _itemToDeleteState.value.toMutableList()
        updatedList.add(id)
        _itemToDeleteState.value = updatedList
    }

    fun onUndoDeleteComment(id: String){
        val updatedList = _itemToDeleteState.value.toMutableList()
        updatedList.remove(id)
        _itemToDeleteState.value = updatedList
    }

    fun onCommitCommentDelete(id: String){
        viewModelScope.launch {
            deleteCommentUseCase(DeleteCommentUseCase.Params(id))
        }
    }

    fun onDeletePoi(){
        viewModelScope.launch {
            val id = idState.value
            deletePoiUseCase(DeletePoiUseCase.Params(id))
            _finishScreenState.emit(true)
        }
    }
}