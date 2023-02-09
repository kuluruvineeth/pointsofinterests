package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val poiRepository: PoiRepository
) : UseCase<DeleteCommentUseCase.Params, Unit>(){

    override suspend fun operation(params: Params) {
        poiRepository.deleteComment(params.id)
    }

    data class Params(val id: String)
}