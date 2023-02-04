package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val repository: CategoriesRepository
) : UseCase<UpdateCategoryUseCase.Params, Unit>(){

    override suspend fun operation(params: Params){
        repository.updateCategory(params.category)
    }

    data class Params(val category: Category)
}