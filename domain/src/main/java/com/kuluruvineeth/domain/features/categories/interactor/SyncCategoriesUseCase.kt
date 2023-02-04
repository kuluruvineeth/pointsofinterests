package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import javax.inject.Inject

class SyncCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository
) : UseCase<Unit, Unit>(){

    override suspend fun operation(params: Unit) {
        repository.sync()
    }
}