package com.kuluruvineeth.pointsofinterests.features.categories.ui.models

import androidx.compose.ui.graphics.Color
import com.kuluruvineeth.domain.features.categories.models.CategoryType

data class CategoryUiModel(
    val id: String,
    val color: Color,
    val title: String,
    val isMutableCategory: Boolean = false,
    val categoryType: CategoryType
)
