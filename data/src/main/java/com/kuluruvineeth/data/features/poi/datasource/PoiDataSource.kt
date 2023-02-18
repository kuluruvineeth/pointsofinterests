package com.kuluruvineeth.data.features.poi.datasource

import com.kuluruvineeth.data.features.poi.model.OrderByColumns
import com.kuluruvineeth.data.features.poi.model.PoiCommentDataModel
import com.kuluruvineeth.data.features.poi.model.PoiDataModel
import com.kuluruvineeth.data.features.poi.model.PoiStatisticsDataModel
import kotlinx.coroutines.flow.Flow

interface PoiDataSource {

    fun getPoiList(orderByOption: OrderByColumns): Flow<List<PoiDataModel>>
    fun getUsedCategoriesIds(): Flow<List<Int>>
    suspend fun searchPoi(query: String): List<PoiDataModel>
    suspend fun getPoi(id: String): PoiDataModel
    suspend fun insertPoi(dataModel: PoiDataModel)
    suspend fun deletePoi(id: String)
    suspend fun deletePoiOlderThen(daysCount: Int): List<PoiDataModel>
    suspend fun getStatistics(): PoiStatisticsDataModel

    fun getComments(parentId: String): Flow<List<PoiCommentDataModel>>
    suspend fun addComment(comment: PoiCommentDataModel)
    suspend fun deleteComment(id: String)
}