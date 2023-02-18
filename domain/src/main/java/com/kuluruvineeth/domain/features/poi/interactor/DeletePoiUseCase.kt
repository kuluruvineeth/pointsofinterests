package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.di.UseCaseDispatcher
import com.kuluruvineeth.domain.features.poi.models.PoiModel
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeletePoiUseCase @Inject constructor(
    private val repository: PoiRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<DeletePoiUseCase.Params, Unit>(dispatcher) {

    override suspend fun operation(params: Params) {
        repository.deletePoi(params.model)
    }

    data class Params(val model: PoiModel)
}