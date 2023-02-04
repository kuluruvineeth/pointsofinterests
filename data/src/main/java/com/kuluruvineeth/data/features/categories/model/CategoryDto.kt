package com.kuluruvineeth.data.features.categories.model

import com.kuluruvineeth.data.core.UNSPECIFIED_ID
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.domain.features.categories.models.CreateCategoryPayload

data class CategoryDto(
    val id: Int,
    val title: String,
    val color: Int,
    val type: String,
    val isMutable: Boolean
)

fun CategoryDto.toDomain() = Category(
    id = id.toString(),
    title = title,
    color = color,
    categoryType = type.toCategoryType(),
    isMutable = isMutable
)

fun Category.toDto() = CategoryDto(
    id = id.toInt(),
    title = title,
    color = color,
    type = categoryType.name,
    isMutable = isMutable
)

fun CreateCategoryPayload.toDto() = CategoryDto(
    id = UNSPECIFIED_ID,
    title = title,
    color = color,
    type = CategoryType.PERSONAL.name,
    isMutable = true
)

private fun String.toCategoryType() = CategoryType.valueOf(this)
