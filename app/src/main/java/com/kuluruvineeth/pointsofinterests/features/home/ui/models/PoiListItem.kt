package com.kuluruvineeth.pointsofinterests.features.home.ui.models

import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel

data class PoiListItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val source: String?,
    val imageUrl: String?,
    val modifiedDate: String,
    val notesCount: Int,
    val categories: List<CategoryUiModel>
)
