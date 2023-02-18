package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.core.UseCase
import com.kuluruvineeth.domain.di.UseCaseDispatcher
import com.kuluruvineeth.domain.features.poi.models.WizardSuggestion
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetWizardSuggestionUseCase @Inject constructor(
    private val repository: PoiRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : UseCase<GetWizardSuggestionUseCase.Params, WizardSuggestion>(dispatcher) {

    override suspend fun operation(params: Params): WizardSuggestion {
        return repository.getWizardSuggestion(params.contentUrl)
    }

    data class Params(val contentUrl: String)
}