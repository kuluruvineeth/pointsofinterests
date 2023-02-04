package com.kuluruvineeth.pointsofinterests.features.main.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.domain.features.categories.interactor.SyncCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val syncCategoriesUseCase: SyncCategoriesUseCase
) : ViewModel(), DefaultLifecycleObserver{

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    override fun onCreate(owner: LifecycleOwner) {
        viewModelScope.launch {
            syncCategoriesUseCase(Unit)
            _uiState.value = MainUiState.Success
        }
    }
}

sealed class MainUiState{

    object Loading : MainUiState()
    object Success : MainUiState()
}