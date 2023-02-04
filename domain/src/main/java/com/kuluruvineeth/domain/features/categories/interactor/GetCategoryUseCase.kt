package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.core.FlowUseCase
import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val repository: CategoriesRepository
) : FlowUseCase<GetCategoryUseCase.Params, Category>(){

    override suspend fun operation(params: Params): Flow<Category> =
        repository.getCategory(params.categoryId)

    data class Params(val categoryId: String)
}