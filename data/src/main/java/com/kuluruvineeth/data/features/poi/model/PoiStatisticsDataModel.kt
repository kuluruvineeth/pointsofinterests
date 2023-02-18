package com.kuluruvineeth.data.features.poi.model

import com.kuluruvineeth.domain.features.poi.models.PoiStatisticsSnapshot
import kotlinx.datetime.Instant

data class PoiStatisticsDataModel(
    val categoriesUsage: Map<String, Int>,
    val viewedCount: Int,
    val unViewedCount: Int,
    val history: Map<Instant,Int>
)

fun PoiStatisticsDataModel.toDomain() = PoiStatisticsSnapshot(
    categoriesUsageCount = categoriesUsage,
    viewedPoiCount = viewedCount,
    unViewedPoiCount = unViewedCount,
    poiAdditionsHistory = history
)
