package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class DeleteGarbageUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<Unit,Unit>(){

    override suspend fun operation(params: Unit) {
        repository.deleteGarbage()
    }
}