package com.kuluruvineeth.domain.features.poi.models

import com.kuluruvineeth.domain.features.categories.models.Category
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.Date

data class PoiModel(
    val id: String,
    val title: String,
    val body: String?,
    val imageUrl: String?,
    val creationDate: Instant,
    val source: String?,
    val contentLink: String?,
    val commentsCount: Int,
    val categories: List<Category>
){
    companion object{
        val EMPTY = PoiModel(
            id = "",
            title = "",
            body = null,
            imageUrl = null,
            creationDate = Clock.System.now(),
            source = "",
            contentLink = "",
            commentsCount = 0,
            categories = emptyList()
        )
    }
}
