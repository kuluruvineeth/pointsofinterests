package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.FlowUseCase
import com.kuluruvineeth.domain.di.UseCaseDispatcher

import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsedCategoriesUseCase @Inject constructor(
    private val repository: PoiRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<Int>>(dispatcher) {
    override fun operation(params: Unit): Flow<List<Int>> = repository.getUsedCategories()
}