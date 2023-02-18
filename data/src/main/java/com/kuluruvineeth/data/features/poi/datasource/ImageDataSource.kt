package com.kuluruvineeth.data.features.poi.datasource

interface ImageDataSource {

    suspend fun copyLocalImage(uri: String): String

    suspend fun deleteImage(uri: String)
}