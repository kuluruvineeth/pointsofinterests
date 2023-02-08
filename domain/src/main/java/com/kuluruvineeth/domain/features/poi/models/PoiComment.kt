package com.kuluruvineeth.domain.features.poi.models

import java.util.Date

data class PoiComment(
    val id: String,
    val message: String,
    val commentData: Date
)
