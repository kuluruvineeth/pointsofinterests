package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.FlowUseCase
import com.kuluruvineeth.domain.di.UseCaseDispatcher
import com.kuluruvineeth.domain.features.poi.models.PoiModel
import com.kuluruvineeth.domain.features.poi.models.PoiSortOption
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPoiListUseCase @Inject constructor(
    private val repository: PoiRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<GetPoiListUseCase.Params?, List<PoiModel>>(dispatcher) {

    override fun operation(params: Params?): Flow<List<PoiModel>> {
        return repository.getPoiList(params?.sortOption)
    }

    data class Params(val sortOption: PoiSortOption)
}