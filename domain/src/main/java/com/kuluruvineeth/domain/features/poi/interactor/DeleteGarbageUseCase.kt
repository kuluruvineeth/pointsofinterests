package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class DeleteGarbageUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<Unit,Int>(){

    override suspend fun operation(params: Unit): Int {
        return repository.deleteGarbage()
    }
}