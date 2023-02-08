package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.poi.models.PoiCommentPayload
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<AddCommentUseCase.Params, Unit>(){

    override suspend fun operation(params: Params) {
        repository.addComment(params.targetId, params.payload)
    }

    data class Params(val targetId: String, val payload: PoiCommentPayload)
}