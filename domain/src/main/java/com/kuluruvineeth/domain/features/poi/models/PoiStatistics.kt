package com.kuluruvineeth.domain.features.poi.models

import com.kuluruvineeth.domain.features.categories.models.Category
import kotlinx.datetime.Instant


data class PoiStatistics(
    val viewsStatistics: PoiViewsStatistics,
    val categoriesUsageStatistics: PoiCategoriesUsageStatistics,
    val creationStatistics: PoiCreationStatistics
)

data class PoiViewsStatistics(
    val viewedCount: Int,
    val unViewedCount: Int
)

data class PoiCategoriesUsageStatistics(
    val categoriesStatistic: Map<Category, Int>
)

data class PoiCreationStatistics(
    val countPerData: Map<Instant, Int>
)
