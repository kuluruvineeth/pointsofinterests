package com.kuluruvineeth.pointofinterest.features.home.ui.models

import com.kuluruvineeth.domain.features.poi.models.PoiModel
import com.kuluruvineeth.pointofinterest.core.utils.CommonFormats.FORMAT_DATE_WITH_MONTH_NAME
import com.kuluruvineeth.pointofinterest.core.utils.toStringWithFormat
import com.kuluruvineeth.pointofinterest.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointofinterest.features.categories.ui.models.toUiModel

data class PoiListItem(
    val id: String,
    val title: String,
    val subtitle: String?,
    val source: String?,
    val imageUrl: String?,
    val modifiedDate: String,
    val commentsCount: Int,
    val categories: List<CategoryUiModel>
)

fun PoiModel.toListUiModel() = PoiListItem(
    id = id,
    title = title,
    subtitle = body,
    source = source,
    imageUrl = imageUrl,
    commentsCount = commentsCount,
    modifiedDate = creationDate.toStringWithFormat(FORMAT_DATE_WITH_MONTH_NAME),
    categories = categories.map { it.toUiModel() }
)