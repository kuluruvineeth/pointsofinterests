package com.kuluruvineeth.domain.features.poi.models

import kotlinx.datetime.Instant
import java.util.Date

data class PoiComment(
    val id: String,
    val message: String,
    val commentDate: Instant
)
