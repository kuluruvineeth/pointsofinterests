package com.kuluruvineeth.data.features.poi.model

import org.junit.Test
import kotlin.test.assertEquals

class WizardSuggestionDataModelTest {

    @Test
    fun test_WizardSuggestionDataModel_toDomain_function_returns_WizardSuggestion_model_with_correct_fields(){

        val dataModel = WizardSuggestionDataModel(
            contentUrl = "https://www.google.com",
            title = "Title",
            body = "Suggestion body",
            imageUrl = "https://www.google.com/image"
        )

        val domainModel = dataModel.toDomain()

        assertEquals(dataModel.contentUrl, domainModel.contentUrl)
        assertEquals(dataModel.title, domainModel.title)
        assertEquals(dataModel.body, domainModel.body)
        assertEquals(dataModel.imageUrl, domainModel.imageUrl)
        assertEquals(null, domainModel.tags)
    }
}