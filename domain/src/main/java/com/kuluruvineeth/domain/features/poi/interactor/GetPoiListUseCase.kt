package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.FlowUseCase
import com.kuluruvineeth.domain.features.poi.models.PoiSnapshotModel
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPoiListUseCase @Inject constructor(
    private val repository: PoiRepository
) : FlowUseCase<Unit, List<PoiSnapshotModel>>(){

    override fun operation(params: Unit): Flow<List<PoiSnapshotModel>> {
        return repository.getPoiList()
    }
}