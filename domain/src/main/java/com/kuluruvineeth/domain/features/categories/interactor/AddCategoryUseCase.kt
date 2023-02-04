package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.categories.models.CreateCategoryPayload
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import javax.inject.Inject

class AddCategoryUseCase @Inject constructor(
    private val repository: CategoriesRepository
) : UseCase<AddCategoryUseCase.Params, Unit>(){

    override suspend fun operation(params: Params) {
        repository.addCategory(params.toPayload())
    }

    private fun Params.toPayload() = CreateCategoryPayload(title,color)

    data class Params(
        var title: String,
        val color: Int
    )
}