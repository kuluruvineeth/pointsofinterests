package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.core.FlowUseCase
import com.kuluruvineeth.domain.di.UseCaseDispatcher
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesByIdsUseCase @Inject constructor(
    private val repository: CategoriesRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<GetCategoriesByIdsUseCase.Params, List<Category>>(dispatcher) {

    override fun operation(params: Params): Flow<List<Category>> = repository.getCategories(params.ids)

    data class Params(val ids: List<Int>)
}