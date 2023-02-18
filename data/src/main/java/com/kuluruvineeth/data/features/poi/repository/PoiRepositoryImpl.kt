package com.kuluruvineeth.data.features.poi.repository

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.kuluruvineeth.data.core.Local
import com.kuluruvineeth.data.core.Remote
import com.kuluruvineeth.data.features.poi.datasource.*
import com.kuluruvineeth.data.features.poi.model.creationDataModel
import com.kuluruvineeth.data.features.poi.model.toDomain
import com.kuluruvineeth.data.features.poi.model.toOrderBy
import com.kuluruvineeth.domain.features.poi.models.*
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PoiRepositoryImpl @Inject constructor(
    @Local private val localDataSource: PoiDataSource,
    @Local private val imageDataSource: ImageDataSource,
    @Remote private val wizardRemoteDataSource: WizardDataSource
) : PoiRepository{
    override fun getPoiList(sortOption: PoiSortOption?): Flow<List<PoiModel?>> =
        localDataSource.getPoiList((sortOption ?: PoiSortOption.DATE).toOrderBy())
            .map { list -> list.map { it.toDomain() } }

    override fun getUsedCategories(): Flow<List<Int>> =
        localDataSource.getUsedCategoriesIds()

    override suspend fun searchPoi(query: String): List<PoiModel> =
        localDataSource.searchPoi(query).map{it.toDomain()!!}


    override suspend fun getDetailedPoi(id: String): PoiModel? =
        localDataSource.getPoi(id).toDomain()

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.Q)
    override suspend fun createPoi(payload: PoiCreationPayload) {
        val finalPayload: PoiCreationPayload = if
                (payload.imageUrl?.startsWith(CONTENT_URI_PREFIX) == true){
            val internalImageUri =
                imageDataSource.copyLocalImage(requireNotNull(payload.imageUrl))
                payload.copy(imageUrl = internalImageUri)
        }else{
            payload
        }
        localDataSource.insertPoi(finalPayload.creationDataModel())
    }

    override suspend fun deletePoi(model: PoiModel) {
        localDataSource.deletePoi(model.id)
        model.imageUrl?.takeIf { it.startsWith(LOCAL_IMAGE_PREFIX) }?.let { uri ->
            imageDataSource.deleteImage(uri)
        }
    }

    override suspend fun deleteGarbage() : Int{
        val deletedItems = localDataSource.deletePoiOlderThen(GARBAGE_DAYS_THRESHOLD)
        deletedItems.filter { item ->
            item.imageUrl?.startsWith(LOCAL_IMAGE_PREFIX) == true
        }.forEach { model ->
            if(model.imageUrl != null){
                imageDataSource.deleteImage(model.imageUrl)
            }
        }
        return deletedItems.size
    }

    override suspend fun addComment(targetId: String, payload: PoiCommentPayload) {
        localDataSource.addComment(payload.creationDataModel(targetId.toInt()))
    }

    override fun getComments(targetId: String): Flow<List<PoiComment>> =
        localDataSource.getComments(targetId).map { list -> list.map {
            it.toDomain()
        } }


    override suspend fun deleteComment(id: String) {
        localDataSource.deleteComment(id)
    }

    override suspend fun getWizardSuggestion(contentUrl: String): WizardSuggestion =
        wizardRemoteDataSource.getWizardSuggestion(contentUrl).toDomain()

    companion object{
        private const val GARBAGE_DAYS_THRESHOLD = 90
        private const val CONTENT_URI_PREFIX = "content://"
        private const val LOCAL_IMAGE_PREFIX = "file:///"
    }

}