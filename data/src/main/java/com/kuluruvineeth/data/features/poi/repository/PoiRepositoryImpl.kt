package com.kuluruvineeth.data.features.poi.repository

import com.kuluruvineeth.data.core.Local
import com.kuluruvineeth.data.features.poi.datasource.PoiDataSource
import com.kuluruvineeth.data.features.poi.model.creationDataModel
import com.kuluruvineeth.data.features.poi.model.toDomain
import com.kuluruvineeth.data.features.poi.model.toOrderBy
import com.kuluruvineeth.domain.features.poi.models.*
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PoiRepositoryImpl @Inject constructor(
    @Local private val localDataSource: PoiDataSource
) : PoiRepository{
    override fun getPoiList(sortOption: PoiSortOption?): Flow<List<PoiModel?>> =
        localDataSource.getPoiList((sortOption ?: PoiSortOption.DATE).toOrderBy())
            .map { list -> list.map { it.toDomain() } }

    override fun getUsedCategories(): Flow<List<Int>> =
        localDataSource.getUsedCategoriesIds()


    override suspend fun getDetailedPoi(id: String): PoiModel? =
        localDataSource.getPoi(id).toDomain()

    override suspend fun createPoi(payload: PoiCreationPayload) {
        localDataSource.insertPoi(payload.creationDataModel())
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

    override suspend fun getWizardSuggestion(contentUrl: String): WizardSuggestion {
        TODO("Not yet implemented")
    }

}