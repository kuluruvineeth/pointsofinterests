package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.poi.models.PoiModel
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class SearchPoiUseCase @Inject constructor(
    private val poiRepository: PoiRepository
) : UseCase<SearchPoiUseCase.Params, List<PoiModel>>(){

    override suspend fun operation(params: Params): List<PoiModel> =
        poiRepository.searchPoi(params.query)

    data class Params(val query: String)
}