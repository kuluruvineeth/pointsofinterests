package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.poi.models.PoiDetailedModel
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class GetDetailedPoiUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<GetDetailedPoiUseCase.Params, PoiDetailedModel>(){

    override suspend fun operation(params: Params): PoiDetailedModel {
        return repository.getDetailedPoi(params.id)
    }

    data class Params(val id: String)
}