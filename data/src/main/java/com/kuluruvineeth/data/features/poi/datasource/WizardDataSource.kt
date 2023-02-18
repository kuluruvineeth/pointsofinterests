package com.kuluruvineeth.data.features.poi.datasource

import com.kuluruvineeth.data.features.poi.model.WizardSuggestionDataModel

interface WizardDataSource {

    suspend fun getWizardSuggestion(url: String): WizardSuggestionDataModel
}