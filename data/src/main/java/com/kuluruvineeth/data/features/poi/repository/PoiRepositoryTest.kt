package com.kuluruvineeth.data.features.poi.repository

import android.graphics.Color
import android.net.Uri
import com.kuluruvineeth.data.features.poi.datasource.ImageDataSource
import com.kuluruvineeth.data.features.poi.datasource.PoiDataSource
import com.kuluruvineeth.data.features.poi.datasource.WizardDataSource
import com.kuluruvineeth.data.MockitoHelper
import com.kuluruvineeth.data.MockitoHelper.anyNonNull
import com.kuluruvineeth.data.MockitoHelper.argumentCaptor
import com.kuluruvineeth.data.MockitoHelper.capture
import com.kuluruvineeth.data.MockitoHelper.whenever
import com.kuluruvineeth.data.core.UNSPECIFIED_ID
import com.kuluruvineeth.data.features.categories.model.CategoryDataModel
import com.kuluruvineeth.data.features.categories.model.toDataModel
import com.kuluruvineeth.data.features.poi.model.*
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.domain.features.poi.models.PoiCommentPayload
import com.kuluruvineeth.domain.features.poi.models.PoiCreationPayload
import com.kuluruvineeth.domain.features.poi.models.PoiModel
import com.kuluruvineeth.domain.features.poi.models.PoiSortOption
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@RunWith(MockitoJUnitRunner::class)
class PoiRepositoryTest {

    @Mock
    private lateinit var localPoiDataSource: PoiDataSource

    @Mock
    private lateinit var wizardRemoteDataSource: WizardDataSource

    @Mock
    private lateinit var localImageDataSource: ImageDataSource

    private lateinit var uriMockStatic : MockedStatic<Uri>

    private lateinit var SUT: PoiRepository

    @Before
    fun setup(){

        SUT = PoiRepositoryImpl(localPoiDataSource, localImageDataSource, wizardRemoteDataSource)

        val mockUri = MockitoHelper.mock<Uri>()
        uriMockStatic = Mockito.mockStatic(Uri::class.java)
        uriMockStatic.`when`<Uri> { Uri.parse("https://www.google.com/somethingelse?query=1") }.thenReturn(mockUri)
        whenever(mockUri.scheme).thenReturn("http")
        whenever(mockUri.host).thenReturn("www.google.com")
    }

    @After
    fun dispose(){
        uriMockStatic.close()
    }

    @Test
    fun test_getPoiList_invokes_local_poi_data_source_getPoiList_function_with_correct_sort_options() = runTest {

        val testPoiList = testPoiDataModels()
        val sortOption = PoiSortOption.SEVERITY
        whenever(localPoiDataSource.getPoiList(anyNonNull())).thenReturn(flowOf(testPoiList))

        val result = SUT.getPoiList(sortOption).first()
        val captor = argumentCaptor<OrderByColumns>()

        verify(localPoiDataSource, times(1)).getPoiList(capture(captor))
        assertEquals(OrderByColumns.SEVERITY, captor.allValues[0])
        Assert.assertArrayEquals(testPoiList.map { it.toDomain() }.toTypedArray(), result.toTypedArray())
    }

    @Test
    fun test_getPoiList_invokes_local_poi_data_source_getPoiList_function_with_default_sort_options_when_argument_is_null() = runTest {

        val testPoiList = testPoiDataModels()
        whenever(localPoiDataSource.getPoiList(anyNonNull())).thenReturn(flowOf(testPoiList))

        SUT.getPoiList(null).first()
        val captor = argumentCaptor<OrderByColumns>()

        verify(localPoiDataSource, times(1)).getPoiList(capture(captor))
        assertEquals(OrderByColumns.DATE, captor.value)
    }

    @Test
    fun test_getUsedCategories_invokes_local_data_source_getUsedCategories_function() = runTest {

        val categories = arrayListOf(1, 2, 3)
        whenever(localPoiDataSource.getUsedCategoriesIds()).thenReturn(flowOf(categories))

        val result = SUT.getUsedCategories().first()
        verify(localPoiDataSource, times(1)).getUsedCategoriesIds()

        Assert.assertArrayEquals(categories.toTypedArray(), result.toTypedArray())
    }

    @Test
    fun test_searchPoi_invokes_local_data_source_searchPoi_function_with_same_query() = runTest {

        val query = "Title 1"
        val testPoiList = testPoiDataModels()
        whenever(localPoiDataSource.searchPoi(anyNonNull())).thenReturn(testPoiList)

        val result = SUT.searchPoi(query)
        val captor = argumentCaptor<String>()
        verify(localPoiDataSource, times(1)).searchPoi(capture(captor))

        assertEquals(query, captor.value)
        Assert.assertArrayEquals(testPoiList.map { it.toDomain() }.toTypedArray(), result.toTypedArray())
    }

    @Test
    fun test_getDetailedPoi_invokes_local_data_source_getPoi_function() = runTest {

        val id = "1"
        val fakePoi = PoiDataModel(
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

        whenever(localPoiDataSource.getPoi(anyNonNull())).thenReturn(fakePoi)

        val result = SUT.getDetailedPoi(id)
        val captor = argumentCaptor<String>()
        verify(localPoiDataSource, times(1)).getPoi(capture(captor))

        assertEquals(id, captor.value)
        assertEquals(fakePoi.toDomain(), result)
    }

    @Test
    fun test_createPoi_invokes_local_data_source_insertPoi_function_and_not_invoke_local_image_data_source_copyLocalImage_with_remote_image() = runTest {

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
        whenever(localPoiDataSource.insertPoi(anyNonNull())).thenReturn(Unit)

        SUT.createPoi(payload)
        val captor = argumentCaptor<PoiDataModel>()

        verify(localPoiDataSource, times(1)).insertPoi(capture(captor))
        verify(localImageDataSource, times(0)).copyLocalImage(anyNonNull())


        assertEquals(UNSPECIFIED_ID, captor.value.id)
        assertEquals(payload.title, captor.value.title)
        assertEquals(payload.body, captor.value.body)
        assertEquals(payload.imageUrl, captor.value.imageUrl)
        assertEquals(payload.contentLink, captor.value.contentLink)
        Assert.assertArrayEquals(payload.categories.map { it.toDataModel() }.toTypedArray(), captor.value.categories.toTypedArray())
    }

    @Test
    fun test_createPoi_invokes_local_data_source_insertPoi_function_and_local_image_data_source_copyLocalImage_with_local_image_uri() = runTest {

        val payload = PoiCreationPayload(
            title = "Title",
            body = "Body",
            contentLink = null,
            imageUrl = "content:///file/something/image.png",
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

        val fileImagePath = "file:///storage/poi_1235.jpg"

        whenever(localPoiDataSource.insertPoi(anyNonNull())).thenReturn(Unit)
        whenever(localImageDataSource.copyLocalImage(anyNonNull())).thenReturn(fileImagePath)

        SUT.createPoi(payload)
        val captor = argumentCaptor<PoiDataModel>()
        val captorImageUri = argumentCaptor<String>()

        verify(localPoiDataSource, times(1)).insertPoi(capture(captor))
        verify(localImageDataSource, times(1)).copyLocalImage(capture(captorImageUri))

        assertEquals(UNSPECIFIED_ID, captor.value.id)
        assertEquals(payload.title, captor.value.title)
        assertEquals(payload.body, captor.value.body)
        assertEquals(payload.imageUrl, captorImageUri.value)
        assertEquals(fileImagePath, captor.value.imageUrl)
        assertEquals(payload.contentLink, captor.value.contentLink)
        Assert.assertArrayEquals(payload.categories.map { it.toDataModel() }.toTypedArray(), captor.value.categories.toTypedArray())
    }

    @Test
    fun test_deletePoi_invokes_local_data_source_deletePoi_function_and_local_image_data_source_deleteImage_with_local_image_uri() = runTest {

        val poiModel = PoiModel(
            id = "1",
            title = "Title",
            body = "Body",
            imageUrl = "https://www.google.com/image",
            source = "www.google.com",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 2,
            categories = arrayListOf(Category("1", "Name", Color.WHITE, categoryType = CategoryType.PERSONAL, isMutable = true))
        )

        whenever(localPoiDataSource.deletePoi(anyNonNull())).thenReturn(Unit)

        SUT.deletePoi(poiModel)
        val captor = argumentCaptor<String>()

        verify(localPoiDataSource, times(1)).deletePoi(capture(captor))
        verify(localImageDataSource, times(0)).deleteImage(anyNonNull())
        assertEquals(poiModel.id, captor.value)
    }

    @Test
    fun test_deletePoi_invokes_local_data_source_deletePoi_function_and_not_invoke_local_image_data_source_deleteImage_with_remote_image_uri() = runTest {

        val fileImagePath = "file:///storage/poi_1235.jpg"

        val poiModel = PoiModel(
            id = "1",
            title = "Title",
            body = "Body",
            imageUrl = fileImagePath,
            source = "www.google.com",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 2,
            categories = arrayListOf(Category("1", "Name", Color.WHITE, categoryType = CategoryType.PERSONAL, isMutable = true))
        )

        whenever(localPoiDataSource.deletePoi(anyNonNull())).thenReturn(Unit)
        whenever(localImageDataSource.deleteImage(anyNonNull())).thenReturn(Unit)

        SUT.deletePoi(poiModel)
        val captor = argumentCaptor<String>()
        val captorImageUri = argumentCaptor<String>()

        verify(localPoiDataSource, times(1)).deletePoi(capture(captor))
        verify(localImageDataSource, times(1)).deleteImage(capture(captorImageUri))

        assertEquals(poiModel.id, captor.value)
        assertEquals(poiModel.imageUrl, captorImageUri.value)
    }

    @Test
    fun test_addComment_invokes_local_data_source_addComment_function() = runTest {

        val payload = PoiCommentPayload(body = "Message")
        val parentId = "1"

        whenever(localPoiDataSource.addComment(anyNonNull())).thenReturn(Unit)

        SUT.addComment(parentId, payload)
        val commentCaptor = argumentCaptor<PoiCommentDataModel>()

        verify(localPoiDataSource, times(1)).addComment(capture(commentCaptor))

        assertEquals(UNSPECIFIED_ID, commentCaptor.value.id)
        assertEquals(parentId, commentCaptor.value.parentId.toString())
        assertEquals(payload.body, commentCaptor.value.body)
        assertNotNull(commentCaptor.value.creationDataTime)
    }

    @Test
    fun test_deleteComment_invokes_local_data_source_deleteComment_function() = runTest {

        val commentId = "1"
        whenever(localPoiDataSource.deleteComment(anyNonNull())).thenReturn(Unit)
        SUT.deleteComment(commentId)
        val captor = argumentCaptor<String>()
        verify(localPoiDataSource, times(1)).deleteComment(capture(captor))
        assertEquals(commentId, captor.value)
    }

    @Test
    fun test_getComments_invokes_local_data_source_getComments_function() = runTest {

        val parentId = "105"
        val fakeComments = arrayListOf(
            PoiCommentDataModel(
                parentId = 105,
                id = 1,
                body = "Comment body 1",
                creationDataTime = Clock.System.now()
            ),
            PoiCommentDataModel(
                parentId = 105,
                id = 2,
                body = "Comment body 2",
                creationDataTime = Clock.System.now()
            )
        )

        whenever(localPoiDataSource.getComments(anyNonNull())).thenReturn(flowOf(fakeComments))
        val result = SUT.getComments(parentId).first()
        val captor = argumentCaptor<String>()
        verify(localPoiDataSource, times(1)).getComments(capture(captor))
        assertEquals(parentId, captor.value)
        Assert.assertArrayEquals(fakeComments.map { it.toDomain() }.toTypedArray(), result.toTypedArray())
    }

    @Test
    fun test_getWizardSuggestion_invokes_remote_wizard_data_source_getWizardSuggestion_function() = runTest {

        val contentUrl = "https://www.google.com"
        val fakeWizardSuggestion = WizardSuggestionDataModel(
            contentUrl = "https://www.google.com",
            title = "Title",
            body = "Suggestion body",
            imageUrl = "https://www.google.com/image"
        )
        whenever(wizardRemoteDataSource.getWizardSuggestion(anyNonNull())).thenReturn(fakeWizardSuggestion)
        val result = SUT.getWizardSuggestion(contentUrl)
        val captor = argumentCaptor<String>()
        verify(wizardRemoteDataSource, times(1)).getWizardSuggestion(capture(captor))
        assertEquals(contentUrl, captor.value)
        assertEquals(fakeWizardSuggestion.toDomain(), result)
    }

    private fun testPoiDataModels(): List<PoiDataModel> {
        val poiStub1 = PoiDataModel(
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

        val poiStub2 = PoiDataModel(
            id = 2,
            title = "Title 2",
            body = "Body 2",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 0,
            severity = 1,
            categories = arrayListOf(CategoryDataModel(2, "Name 2", Color.GRAY, type = "PERSONAL", true))
        )

        val poiStub3 = PoiDataModel(
            id = 3,
            title = "Title 3",
            body = "Body 3",
            imageUrl = "https://www.google.com/image",
            contentLink = "https://www.google.com/somethingelse?query=1",
            creationDate = Clock.System.now(),
            commentsCount = 10,
            severity = 0,
            categories = arrayListOf(CategoryDataModel(3, "Name 3", Color.GREEN, type = "PERSONAL", true))
        )

        return arrayListOf(poiStub1, poiStub2, poiStub3)
    }
}