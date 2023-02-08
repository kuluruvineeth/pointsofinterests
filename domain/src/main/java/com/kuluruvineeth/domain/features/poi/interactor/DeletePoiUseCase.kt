package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class DeletePoiUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<DeletePoiUseCase.Params, Unit>(){

    override suspend fun operation(params: Params) {
        repository.deletePoi(params.id)
    }

    data class Params(val id: String)
}