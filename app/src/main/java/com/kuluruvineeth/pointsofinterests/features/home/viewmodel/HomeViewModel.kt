package com.kuluruvineeth.pointsofinterests.features.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.pointsofinterests.core.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel(){

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun onLaunched(){
        fetchData()
    }

    fun onRetry(){
        fetchData()
    }

    private fun fetchData(){
        viewModelScope.launch(IO) {
            _uiState.emit(UiState.Loading)
            delay(2000)
            val random = Random.nextBoolean()
            if(random){
                _uiState.emit(UiState.Empty)
            }else{
                _uiState.emit(UiState.Error("Failed to Fetch data from server"))
            }
        }
    }
}