package com.kuluruvineeth.domain.features.categories.repo

import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.models.CreateCategoryPayload
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {

    suspend fun sync()

    fun getCategories(): Flow<List<Category>>

    fun getCategories(ids: List<Int>): Flow<List<Category>>

    fun getCategories(type: CategoryType): Flow<List<Category>>

    suspend fun getCategory(id: String): Category

    suspend fun addCategory(payload: CreateCategoryPayload)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(categoryId: String)
}