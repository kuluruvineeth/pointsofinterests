package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.poi.models.PoiCreationPayload
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class CreatePoiUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<CreatePoiUseCase.Params, String>(){

    override suspend fun operation(params: Params): String {
        return repository.createPoi(params.payload)
    }

    data class Params(val payload: PoiCreationPayload)
}