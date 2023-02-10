package com.kuluruvineeth.pointsofinterests.features.poi.create.models

import com.kuluruvineeth.domain.features.poi.models.WizardSuggestion

data class WizardSuggestionUiModel(
    val url: String? = null,
    val title: String? = null,
    val body: String? = null,
    val imageUrl: String? = null,
    val tags: List<String>? = null
){

    fun isSingleImageSuggestion() = imageUrl != null && title == null && body == null

    companion object{
        val EMPTY = WizardSuggestionUiModel()
    }

}

fun WizardSuggestion.toUiModel() = WizardSuggestionUiModel(
    url = contentUrl,
    title = title,
    body = body,
    imageUrl = imageUrl,
    tags = tags
)
