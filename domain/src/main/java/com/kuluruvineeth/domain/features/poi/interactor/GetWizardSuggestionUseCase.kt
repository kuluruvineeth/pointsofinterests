package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.features.poi.models.WizardSuggestion
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import javax.inject.Inject

class GetWizardSuggestionUseCase @Inject constructor(
    private val repository: PoiRepository
) : UseCase<GetWizardSuggestionUseCase.Params, WizardSuggestion>(){

    override suspend fun operation(params: Params): WizardSuggestion {
        return repository.getWizardSuggestion(params.contentUrl)
    }

    data class Params(val contentUrl: String)
}