package com.kuluruvineeth.pointsofinterests.features.home.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.pointsofinterests.core.utils.RetryTrigger
import com.kuluruvineeth.pointsofinterests.core.utils.retryableFlow
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.PoiListItem
import com.kuluruvineeth.pointsofinterests.features.home.ui.models.PoiSortByOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel(){

    val categoriesState = collectCategories().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val retryTrigger by lazy { RetryTrigger() }


    val homeUiContentState = retryableFlow(retryTrigger){
        collectPoi().map{
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

    fun onApplySortBy(option: PoiSortByOption){

    }

    private fun collectCategories() : Flow<List<CategoryUiModel>> = flow {
        delay(1000)
        emit(mockCategories())
    }

    private fun collectPoi(): Flow<List<PoiListItem>> = flow {
        delay(2000)
        emit(mockPoi())
    }

    private fun mockCategories(): List<CategoryUiModel> = arrayListOf(
        CategoryUiModel(id = "_ID3", title = "High", color = Color(0xFFD50000), categoryType = CategoryType.SEVERITY),
        CategoryUiModel(id = "_ID5", title = "Medium", color = Color(0xFFFF9800), categoryType = CategoryType.SEVERITY),
        CategoryUiModel(id = "_ID6", title = "Low", color = Color(0xFF7CB342), categoryType = CategoryType.SEVERITY),
        CategoryUiModel(id = "_ID", title = "Business", color = Color(0xFF2980B9), categoryType = CategoryType.GLOBAL),
        CategoryUiModel(id = "_ID2", title = "Music", color = Color(0xFF9C27B0), categoryType = CategoryType.GLOBAL),
        CategoryUiModel(id = "_ID7", title = "TV Shows", color = Color(0xFFC6FF00), categoryType = CategoryType.GLOBAL),
        CategoryUiModel(id = "_ID8", title = "Home decor", color = Color(0xFF00897B), categoryType = CategoryType.PERSONAL),
        CategoryUiModel(id = "_ID9", title = "Sport", color = Color(0xFFFFEB3B), categoryType = CategoryType.PERSONAL),
        CategoryUiModel(id = "_ID10", title = "Android development", color = Color(0xFF76FF03), categoryType = CategoryType.PERSONAL),
    )

    private fun mockPoi(): List<PoiListItem> = arrayListOf<PoiListItem>().apply {
        this += PoiListItem(
            id = "id1",
            title = "New business forum will start soon",
            subtitle = "All biggest companies will present their vision for future",
            source = "meduim.com",
            imageUrl = "https://cdn.pixabay.com/photo/2018/03/27/21/43/startup-3267505_1280.jpg",
            notesCount = 1,
            modifiedDate = "29.10.2022",
            categories = arrayListOf(
                CategoryUiModel(id = "_ID5", title = "Medium", color = Color(0xFFFF9800), categoryType = CategoryType.SEVERITY),
                CategoryUiModel(id = "_ID", title = "Business", color = Color(0xFF2980B9), categoryType = CategoryType.GLOBAL)
            )
        )

        this += PoiListItem(
            id = "id2",
            title = "New version of compose was deployed",
            subtitle = "New features, improved stability, material 3 support and many more",
            source = "androiddeveloper.com",
            imageUrl = "https://cdn.pixabay.com/photo/2018/05/03/21/49/android-3372580_960_720.png",
            notesCount = 10,
            modifiedDate = "15.12.2022",
            categories = arrayListOf(
                CategoryUiModel(id = "_ID3", title = "High", color = Color(0xFFD50000), categoryType = CategoryType.SEVERITY),
                CategoryUiModel(
                    id = "_ID10",
                    title = "Android development",
                    color = Color(0xFF76FF03),
                    categoryType = CategoryType.PERSONAL
                )
            )
        )

        this += PoiListItem(
            id = "id2",
            title = "Fisher",
            subtitle = "New release - Music EP",
            source = "spotify.com",
            imageUrl = null,
            notesCount = 0,
            modifiedDate = "5.06.2022",
            categories = arrayListOf(
                CategoryUiModel(id = "_ID6", title = "Low", color = Color(0xFF7CB342), categoryType = CategoryType.SEVERITY),
                CategoryUiModel(id = "_ID2", title = "Music", color = Color(0xFF9C27B0), categoryType = CategoryType.GLOBAL)
            )
        )
    }
    sealed class HomeUiContentState{
        object Loading : HomeUiContentState()
        object Empty : HomeUiContentState()
        data class Result(val poiList: List<PoiListItem>) : HomeUiContentState()
        data class Error(val message: String) : HomeUiContentState()
    }
}
