package com.kuluruvineeth.data.features.poi.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.kuluruvineeth.data.core.Local
import com.kuluruvineeth.data.features.poi.datasource.LocalImageDataSource
import com.kuluruvineeth.data.features.poi.datasource.PoiDataSource
import com.kuluruvineeth.data.features.poi.datasource.WizardRemoteDataSource
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
    private val imageDataSource: LocalImageDataSource,
    private val wizardRemoteDataSource: WizardRemoteDataSource
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

    override suspend fun deletePoi(id: String) {
        localDataSource.deletePoi(id)
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
        private const val CONTENT_URI_PREFIX = "content://"
    }

}