package com.kuluruvineeth.data.features.poi.model

import android.graphics.Color
import android.net.Uri
import com.kuluruvineeth.data.core.UNSPECIFIED_ID
import com.kuluruvineeth.data.features.categories.model.CategoryDataModel
import com.kuluruvineeth.data.features.categories.model.CategoryEntity
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.domain.features.poi.models.PoiCreationPayload
import kotlinx.datetime.Clock
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(MockitoJUnitRunner::class)
class PoiDataModelTest {

    @Test
    fun test_PoiDataModel_toDomain_function_returns_PoiModel_model_with_correct_fields(){

        val mockUri = mock<Uri>()
        val uriMockStatic: MockedStatic<Uri> = Mockito.mockStatic(Uri::class.java)
        uriMockStatic.`when`<Uri> { Uri.parse("https://www.google.com/somethingelse?query=1") }.thenReturn(mockUri)
        whenever(mockUri.scheme).thenReturn("http")
        whenever(mockUri.host).thenReturn("www.google.com")

        val dataModel = PoiDataModel(
            id = 1,
            title = "Title",
            body = "Body",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 2,
            severity = 3,
            categories = arrayListOf(CategoryDataModel(1, "Name", Color.WHITE, type = "PERSONAL", true))
        )

        val domainModel = dataModel.toDomain()
        val expectedCategory = arrayListOf(
            Category(
                id = "1",
                title = "Name",
                color = Color.WHITE,
                categoryType = CategoryType.PERSONAL,
                isMutable = true
            )
        )

        assertEquals(dataModel.id, domainModel!!.id.toInt())
        assertEquals(dataModel.title, domainModel!!.title)
        assertEquals(dataModel.body, domainModel!!.body)
        assertEquals(dataModel.imageUrl, domainModel!!.imageUrl)
        assertEquals(dataModel.contentLink, domainModel!!.contentLink)
        assertEquals("www.google.com", domainModel!!.source)
        assertEquals(dataModel.creationDate, domainModel!!.creationDate)
        assertEquals(dataModel.commentsCount, domainModel!!.commentsCount)
        Assert.assertArrayEquals(expectedCategory.toTypedArray(), domainModel.categories.toTypedArray())
    }

    @Test
    fun test_PoiCreationPayload_creationDataModel_function_returns_PoiDataModel_model_with_correct_fields(){

        val payload = PoiCreationPayload(
            title = "Title",
            body = "Body",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            categories = arrayListOf(
                Category(
                    id = "1",
                    title = "Name",
                    color = Color.WHITE,
                    categoryType = CategoryType.PERSONAL,
                    isMutable = true
                )
            )
        )

        val dataModel = payload.creationDataModel()

        val expectedCategory = arrayListOf(
            CategoryDataModel(
                id = 1,
                title = "Name",
                color = Color.WHITE,
                type = "PERSONAL",
                isMutable = true
            )
        )

        assertEquals(UNSPECIFIED_ID, dataModel.id)
        assertEquals(payload.title, dataModel.title)
        assertEquals(payload.body, dataModel.body)
        assertEquals(payload.imageUrl, dataModel.imageUrl)
        assertEquals(payload.contentLink, dataModel.contentLink)
        Assert.assertArrayEquals(expectedCategory.toTypedArray(), dataModel.categories.toTypedArray())
    }

    @Test
    fun test_PoiDataModel_toEntity_function_returns_PoiEntity_model_with_correct_fields(){

        val dataModel = PoiDataModel(
            id = 1,
            title = "Title",
            body = "Body",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 2,
            severity = 3,
            categories = arrayListOf(CategoryDataModel(1, "Name", Color.WHITE, type = "PERSONAL", true))
        )

        val entityModel = dataModel.toEntity()

        assertEquals(dataModel.id, entityModel.id)
        assertEquals(dataModel.title, entityModel.title)
        assertEquals(dataModel.body, entityModel.body)
        assertEquals(dataModel.imageUrl, entityModel.imageUrl)
        assertEquals(dataModel.contentLink, entityModel.contentLink)
        assertEquals(dataModel.creationDate, entityModel.creationDateTime)
        assertEquals(dataModel.commentsCount, entityModel.commentsCount)
        assertEquals(dataModel.severity, entityModel.severity)
        assertFalse(entityModel.viewed)
    }

    @Test
    fun test_PoiEntity_toDataModel_function_returns_PoiDataModel_model_with_correct_fields(){

        val entityModel = PoiEntity(
            title = "Title",
            body = "Body",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDateTime = Clock.System.now(),
            commentsCount = 2,
            severity = 3,
            viewed = true
        ).apply {
            id = 1
        }

        val dataModel = entityModel.toDataModel()

        assertEquals(entityModel.id, dataModel.id)
        assertEquals(entityModel.title, dataModel.title)
        assertEquals(entityModel.body, dataModel.body)
        assertEquals(entityModel.imageUrl, dataModel.imageUrl)
        assertEquals(dataModel.contentLink, dataModel.contentLink)
        assertEquals(entityModel.creationDateTime, dataModel.creationDate)
        assertEquals(entityModel.commentsCount, dataModel.commentsCount)
        assertEquals(entityModel.severity, dataModel.severity)
        assertTrue(dataModel.categories.isEmpty())
    }

    @Test
    fun test_PoiWithCategoriesEntity_toDataModel_function_returns_PoiDataModel_model_with_correct_fields(){

        val entityModel = PoiEntity(
            title = "Title",
            body = "Body",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDateTime = Clock.System.now(),
            commentsCount = 2,
            severity = 3,
            viewed = true
        ).apply {
            id = 1
        }

        val categoriesModels =
            arrayListOf(CategoryEntity(title = "Title", color = Color.WHITE, type = "PERSONAL", isMutable = true).apply { id = 1 })

        val poiWithCategories = PoiWithCategoriesEntity(entityModel, categoriesModels)

        val expectedCategoriesDataModel =
            arrayListOf(CategoryDataModel(id = 1, title = "Title", Color.WHITE, type = "PERSONAL", isMutable = true))

        val dataModel = poiWithCategories.toDataModel()

        assertEquals(poiWithCategories.entity.id, dataModel.id)
        assertEquals(poiWithCategories.entity.title, dataModel.title)
        assertEquals(poiWithCategories.entity.body, dataModel.body)
        assertEquals(poiWithCategories.entity.imageUrl, dataModel.imageUrl)
        assertEquals(poiWithCategories.entity.contentLink, dataModel.contentLink)
        assertEquals(poiWithCategories.entity.creationDateTime, dataModel.creationDate)
        assertEquals(poiWithCategories.entity.commentsCount, dataModel.commentsCount)
        assertEquals(poiWithCategories.entity.severity, dataModel.severity)
        Assert.assertArrayEquals(expectedCategoriesDataModel.toTypedArray(), dataModel.categories.toTypedArray())
    }
}