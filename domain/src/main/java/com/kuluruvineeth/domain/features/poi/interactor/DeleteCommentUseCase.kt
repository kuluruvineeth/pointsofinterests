package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.di.UseCaseDispatcher
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val poiRepository: PoiRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<DeleteCommentUseCase.Params, Unit>(dispatcher) {

    override suspend fun operation(params: Params) {
        poiRepository.deleteComment(params.id)
    }

    data class Params(val id: String)
}