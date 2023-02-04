package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val repository: CategoriesRepository
) : UseCase<DeleteCategoryUseCase.Params, Unit>(){

    override suspend fun operation(params: Params){
        repository.deleteCategory(params.categoryId)
    }

    data class Params(val categoryId: String)
}