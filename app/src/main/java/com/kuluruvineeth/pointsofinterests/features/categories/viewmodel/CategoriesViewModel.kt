package com.kuluruvineeth.pointsofinterests.features.categories.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.domain.features.categories.interactor.*
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.pointsofinterests.core.utils.containsId
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.DetailedCategoriesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val addCategoryUseCase: AddCategoryUseCase
) : ViewModel() {


    val detailedCategoriesUiState = MutableStateFlow<DetailedCategoriesUiState>(DetailedCategoriesUiState.Loading)
    val categoriesState = MutableStateFlow<Map<String,List<CategoryUiModel>>>(emptyMap())
    val itemsToDelete = MutableStateFlow<List<String>>(emptyList())

    init {
        viewModelScope.launch {
            categoriesState.emitAll(collectCategories())
        }
    }

    fun onFetchDetailedState(categoryId: String?) {
        viewModelScope.launch {
            detailedCategoriesUiState.value = DetailedCategoriesUiState.Loading
            if (categoryId.isNullOrEmpty()) {
                detailedCategoriesUiState.value = DetailedCategoriesUiState.Success(null)
                return@launch
            }
            delay(1000)
            val selectedCategory = categoriesState.value.values.flatten().find { it.id == categoryId }
            detailedCategoriesUiState.value = DetailedCategoriesUiState.Success(selectedCategory)
        }
    }

    fun onUpdateItem(categoryUiModel: CategoryUiModel, name: String, color: Color) {
        val updatedMap = categoriesState.value.toMutableMap()
        val entry = updatedMap.entries.find { it.value.containsId(categoryUiModel.id) }
        entry?.value?.let {
            val updatedList = entry.value.map {
                if (it.id == categoryUiModel.id) {
                    it.copy(title = name, color = color)
                } else {
                    it
                }
            }

            updatedMap[entry.key] = updatedList
            categoriesState.value = updatedMap
        }
    }

    fun onCreateItem(name: String, color: Color) {
        val updatedMap = categoriesState.value.toMutableMap()
        val personalItemsList = updatedMap["Personal"]?.toMutableList()
        personalItemsList?.let {
            it.add(CategoryUiModel("_IDNEW", title = name, color = color, isMutableCategory = true, categoryType = CategoryType.PERSONAL))
            updatedMap["Personal"] = it
        }
        categoriesState.value = updatedMap
    }

    fun onDeleteItem(id: String){
        val updatedList = itemsToDelete.value.toMutableList()
        updatedList.add(id)
        itemsToDelete.value = updatedList
    }

    fun onUndoDeleteItem(id: String){
        val updatedList = itemsToDelete.value.toMutableList()
        updatedList.remove(id)
        itemsToDelete.value = updatedList
    }

    fun onCommitDeleteItem(id: String){
        val updatedMap = categoriesState.value.toMutableMap()
        val entry = updatedMap.entries.find { it.value.containsId(id) }
        entry?.value?.let {
            val updatedList = entry.value.toMutableList()
            updatedList.removeAll{it.id == id}
            updatedMap[entry.key] = updatedList
            categoriesState.value = updatedMap
        }
        Log.d("AAAA","Item $id deleted")
    }

    private fun collectCategories(): Flow<Map<String,List<CategoryUiModel>>> = flow {
        emit(mockCategories())
    }

    private fun mockCategories(): Map<String, List<CategoryUiModel>> = hashMapOf<String, List<CategoryUiModel>>().apply {

        put(
            "Severity",
            arrayListOf(
                CategoryUiModel(id = "_ID3", title = "High", color = Color(0xFFD50000), categoryType = CategoryType.SEVERITY),
                CategoryUiModel(id = "_ID5", title = "Medium", color = Color(0xFFFF9800), categoryType = CategoryType.SEVERITY),
                CategoryUiModel(id = "_ID6", title = "Low", color = Color(0xFF7CB342), categoryType = CategoryType.SEVERITY)
            )
        )

        put(
            "Global",
            arrayListOf(
                CategoryUiModel(id = "_ID", title = "Business", color = Color(0xFF2980B9), categoryType = CategoryType.GLOBAL),
                CategoryUiModel(id = "_ID2", title = "Music", color = Color(0xFF9C27B0), categoryType = CategoryType.GLOBAL),
                CategoryUiModel(id = "_ID7", title = "Video", color = Color(0xFFC6FF00), categoryType = CategoryType.GLOBAL),
                CategoryUiModel(id = "_ID8", title = "Estate", color = Color(0xFF00897B), categoryType = CategoryType.GLOBAL),
                CategoryUiModel(id = "_ID9", title = "Sport", color = Color(0xFFFFEB3B), categoryType = CategoryType.GLOBAL),
                CategoryUiModel(id = "_ID10", title = "Work", color = Color(0xFF1E40AF), categoryType = CategoryType.GLOBAL)
            )
        )

        put(
            "Personal",
            arrayListOf(
                CategoryUiModel(
                    id = "_ID11",
                    title = "Android development",
                    color = Color(0xFF76FF03),
                    isMutableCategory = true,
                    categoryType = CategoryType.PERSONAL
                ),
                CategoryUiModel(
                    id = "_ID12",
                    title = "Abelton",
                    color = Color(0xFF93C5FD),
                    isMutableCategory = true,
                    categoryType = CategoryType.PERSONAL
                ),
                CategoryUiModel(
                    id = "_ID13",
                    title = "Football",
                    color = Color(0xFFA8A29E),
                    isMutableCategory = true,
                    categoryType = CategoryType.PERSONAL
                ),
                CategoryUiModel(
                    id = "_ID14",
                    title = "Apartments",
                    color = Color(0xFFFB923C),
                    isMutableCategory = true,
                    categoryType = CategoryType.PERSONAL
                ),
            )
        )
    }
}