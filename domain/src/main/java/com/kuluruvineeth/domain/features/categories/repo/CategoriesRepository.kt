package com.kuluruvineeth.domain.features.categories.repo

import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.domain.features.categories.models.CreateCategoryPayload
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    fun getCategories() : Flow<List<Category>>

    fun getCategories(type: CategoryType): Flow<List<Category>>

    suspend fun getCategory(id: String): Category

    suspend fun addCategory(payload: CreateCategoryPayload)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(categoryId: String)
}