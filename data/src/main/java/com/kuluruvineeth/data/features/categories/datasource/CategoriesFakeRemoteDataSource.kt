package com.kuluruvineeth.data.features.categories.datasource

import android.content.Context
import com.kuluruvineeth.data.features.categories.model.CategoryDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoriesFakeRemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : CategoriesDataSource{

    override fun getCategories(): Flow<List<CategoryDto>> = flow {
        emit(loadInitialCategories())
    }

    private suspend fun loadInitialCategories(): List<CategoryDto>{
        delay(5000)
        return emptyList<CategoryDto>()
    }
}