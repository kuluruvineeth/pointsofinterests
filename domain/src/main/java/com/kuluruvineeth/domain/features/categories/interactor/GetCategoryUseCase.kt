package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.di.UseCaseDispatcher
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(
    private val repository: CategoriesRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<GetCategoryUseCase.Params, Category>(dispatcher) {

    override suspend fun operation(params: Params): Category = repository.getCategory(params.categoryId)

    data class Params(val categoryId: String)
}