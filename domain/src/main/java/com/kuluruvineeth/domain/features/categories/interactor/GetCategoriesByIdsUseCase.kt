package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.core.FlowUseCase
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesByIdsUseCase @Inject constructor(
    private val repository: CategoriesRepository
) : FlowUseCase<GetCategoriesByIdsUseCase.Params, List<Category>>(){

    override fun operation(params: Params): Flow<List<Category>> =
        repository.getCategories(params.ids)

    data class Params(val ids: List<Int>)
}