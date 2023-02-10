package com.kuluruvineeth.data.features.poi.model

import com.kuluruvineeth.domain.features.poi.models.WizardSuggestion

data class WizardSuggestionDataModel(
    val contentUrl: String,
    val title: String? = null,
    val body: String? = null,
    val imageUrl: String? = null,
    val tags: List<String>? = null
)

fun WizardSuggestionDataModel.toDomain() = WizardSuggestion(
    contentUrl = contentUrl,
    title = title,
    body = body,
    imageUrl = imageUrl,
    tags = tags
)
