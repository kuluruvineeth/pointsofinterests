package com.kuluruvineeth.pointsofinterests.features.categories.ui.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.pointsofinterests.R

data class CategoryUiModel(
    val id: String,
    val color: Color,
    val title: String,
    val isMutableCategory: Boolean = false,
    val categoryType: CategoryType
)

fun Category.toUiModel() = CategoryUiModel(
    id = id,
    title = title,
    color = Color(color),
    isMutableCategory = isMutable,
    categoryType = categoryType
)

fun CategoryUiModel.toDomainModel() = Category(
    id = id,
    title = title,
    color = color.toArgb(),
    isMutable = isMutableCategory,
    categoryType = categoryType
)

fun CategoryType.toTitle() =
    when(this){
        CategoryType.SEVERITY -> R.string.title_severity
        CategoryType.GLOBAL -> R.string.title_global
        CategoryType.PERSONAL -> R.string.title_personal
    }
