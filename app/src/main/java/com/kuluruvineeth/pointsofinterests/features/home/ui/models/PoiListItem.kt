package com.kuluruvineeth.pointsofinterests.features.home.ui.models

import android.os.Build
import androidx.annotation.RequiresApi
import com.kuluruvineeth.domain.features.poi.models.PoiModel
import com.kuluruvineeth.pointsofinterests.core.utils.CommonFormats.FORMAT_DATE_WITH_MONTH_NAME
import com.kuluruvineeth.pointsofinterests.core.utils.toStringWithFormat
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.toUiModel

data class PoiListItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val source: String?,
    val imageUrl: String?,
    val modifiedDate: String,
    val commentsCount: Int,
    val categories: List<CategoryUiModel>
)

@RequiresApi(Build.VERSION_CODES.O)
fun PoiModel.toListUiModel() = PoiListItem(
    id = id,
    title = title,
    subtitle = body,
    source = source,
    imageUrl = imageUrl,
    commentsCount = commentsCount,
    modifiedDate = creationDate.toStringWithFormat(FORMAT_DATE_WITH_MONTH_NAME),
    categories = categories.map{it.toUiModel()}
)
