package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.FlowUseCase
import com.kuluruvineeth.domain.di.UseCaseDispatcher
import com.kuluruvineeth.domain.features.poi.models.PoiComment
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCommentsUseCase @Inject constructor(
    private val repository: PoiRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<GetCommentsUseCase.Params, List<PoiComment>>(dispatcher) {

    override fun operation(params: Params): Flow<List<PoiComment>> {
        return repository.getComments(params.targetId)
    }
    data class Params(val targetId: String)
}