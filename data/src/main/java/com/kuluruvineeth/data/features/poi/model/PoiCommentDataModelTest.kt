package com.kuluruvineeth.data.features.poi.model

import com.kuluruvineeth.data.core.UNSPECIFIED_ID
import com.kuluruvineeth.domain.features.poi.models.PoiCommentPayload
import kotlinx.datetime.Clock
import org.junit.Test
import kotlin.test.assertEquals

class PoiCommentDataModelTest {

    @Test
    fun test_PoiCommentDataModel_toDomain_function_returns_PoiComment_model_with_correct_fields(){

        val dataModel = PoiCommentDataModel(
            parentId = 105,
            id = 1,
            body = "Comment body",
            creationDataTime = Clock.System.now()
        )

        val domainModel = dataModel.toDomain()

        assertEquals(dataModel.id, domainModel.id.toInt())
        assertEquals(dataModel.body, domainModel.message)
        assertEquals(dataModel.creationDataTime, domainModel.commentDate)
    }

    @Test
    fun test_PoiCommentDataModel_toEntity_function_returns_PoiCommentEntity_model_with_correct_fields(){

        val dataModel = PoiCommentDataModel(
            parentId = 105,
            id = 1,
            body = "Comment body",
            creationDataTime = Clock.System.now()
        )

        val entityModel = dataModel.toEntity()

        assertEquals(dataModel.id, entityModel.id)
        assertEquals(dataModel.body, entityModel.body)
        assertEquals(dataModel.creationDataTime, entityModel.creationDataTime)
        assertEquals(dataModel.parentId, entityModel.parentId)

    }

    @Test
    fun test_PoiCommentPayload_creationDataModelParentId_function_returns_PoiCommentDataModel_model_with_correct_fields(){

        val payload = PoiCommentPayload(body = "Message")
        val parentId = 1

        val dataModel = payload.creationDataModel(parentId)

        assertEquals(payload.body, dataModel.body)
        assertEquals(UNSPECIFIED_ID, dataModel.id)
        assertEquals(parentId, dataModel.parentId)
    }

    @Test
    fun test_PoiCommentEntity_toDataModel_function_returns_PoiCommentDataModel_model_with_correct_fields(){

        val entityModel = PoiCommentEntity(
            parentId = 105,
            body = "Comment body",
            creationDataTime = Clock.System.now()
        ).apply {
            id = 10
        }

        val dataModel = entityModel.toDataModel()

        assertEquals(entityModel.body, dataModel.body)
        assertEquals(entityModel.id, dataModel.id)
        assertEquals(entityModel.parentId, dataModel.parentId)
        assertEquals(entityModel.creationDataTime, dataModel.creationDataTime)
    }
}