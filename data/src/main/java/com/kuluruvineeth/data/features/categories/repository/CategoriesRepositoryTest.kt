package com.kuluruvineeth.data.features.categories.repository

import android.graphics.Color
import com.kuluruvineeth.data.features.categories.datasource.CategoriesDataSource
import com.kuluruvineeth.data.features.categories.model.CategoryDataModel
import com.kuluruvineeth.data.features.categories.model.toDataModel
import com.kuluruvineeth.data.features.categories.model.toDomain
import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.models.CategoryType
import com.kuluruvineeth.domain.features.categories.models.CreateCategoryPayload
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class CategoriesRepositoryTest {

    @Mock
    private lateinit var localCategoriesDataSource: CategoriesDataSource

    @Mock
    private lateinit var remoteCategoriesDataSource: CategoriesDataSource

    private lateinit var SUT: CategoriesRepository

    @Before
    fun setup(){
        SUT = CategoriesRepositoryImpl(remoteCategoriesDataSource, localCategoriesDataSource)
    }

    @Test
    fun test_sync_function_invoke_remote_datasource_when_local_data_source_count_is_0() = runTest {

        val category1 = mock<CategoryDataModel>()
        val category2 = mock<CategoryDataModel>()
        val categories = arrayListOf(category1,category2)

        whenever(localCategoriesDataSource.count()).thenReturn(0)

        whenever(localCategoriesDataSource.addCategories(anyNonNull())).thenReturn(Unit)

        whenever(remoteCategoriesDataSource.getCategories()).thenReturn(flowOf(categories))

        SUT.sync()

        val captor = argumentCaptor<List<CategoryDataModel>>()

        verify(localCategoriesDataSource, times(1)).count()
        verify(remoteCategoriesDataSource, times(1)).getCategories()
        verify(localCategoriesDataSource, times(1)).addCategories(capture(captor))

        Assert.assertArrayEquals(categories.toTypedArray(), captor.value.toTypedArray())
    }

    @Test
    fun test_sync_function_not_invoke_remote_datasource_when_local_data_source_count_is_not_0() = runTest {

        whenever(localCategoriesDataSource.count()).thenReturn(2)

        SUT.sync()

        verify(localCategoriesDataSource, times(1)).count()
        verify(remoteCategoriesDataSource, times(0)).getCategories()
        verify(localCategoriesDataSource, times(0)).addCategories(anyNonNull())
    }

    @Test
    fun test_addCategory_function_invokes_local_data_source_addCategory_function() = runTest {

        val payload = CreateCategoryPayload("Title", Color.WHITE)

        whenever(localCategoriesDataSource.addCategories(anyNonNull())).thenReturn(Unit)
        SUT.addCategory(payload)
        verify(localCategoriesDataSource, times(1)).addCategories(anyNonNull())
    }

    @Test
    fun test_updateCategory_function_invokes_local_data_source_updateCategory_function()  = runTest {
        val category = Category("1","Title",Color.WHITE, categoryType = CategoryType.PERSONAL, isMutable = true)

        whenever(localCategoriesDataSource.updateCategory(anyNonNull())).thenReturn(Unit)
        SUT.updateCategory(category)
        val captor = argumentCaptor<CategoryDataModel>()
        verify(localCategoriesDataSource, times(1)).updateCategory(capture(captor))
        assertEquals(category.toDataModel(), captor.value)
    }

    @Test
    fun test_deleteCategory_function_invokes_local_data_source_deleteCategory_function() = runTest {

        val categoryId = "1"

        whenever(localCategoriesDataSource.deleteCategory(anyNonNull())).thenReturn(Unit)
        SUT.deleteCategory(categoryId)
        val captor = argumentCaptor<Int>()
        verify(localCategoriesDataSource, times(1)).deleteCategory(capture(captor))
        assertEquals(1, captor.value)
    }

    @Test
    fun test_getCategory_function_invokes_local_data_source_getCategory_function() = runTest {

        val id = "1"
        val category = CategoryDataModel(1,"TItle",Color.WHITE, type = CategoryType.PERSONAL.name, isMutable = true)

        whenever(localCategoriesDataSource.getCategory(anyNonNull())).thenReturn(category)

        val result = SUT.getCategory(id)
        val captor = argumentCaptor<Int>()
        verify(localCategoriesDataSource, times(1)).getCategory(capture(captor))

        assertEquals(id, captor.value.toString())
        assertEquals(category.toDomain(), result)
    }

    @Test
    fun test_getCategories_function_invokes_local_data_source_getCategories_function() = runTest {

        val category1 = CategoryDataModel(1,"Title",Color.WHITE, type = CategoryType.PERSONAL.name, isMutable = true)
        val category2 = CategoryDataModel(2,"Title",Color.BLACK, type = CategoryType.PERSONAL.name, isMutable = true)
        val categories = arrayListOf(category1,category2)

        whenever(localCategoriesDataSource.getCategories()).thenReturn(flowOf(categories))

        val result = SUT.getCategories().first()
        verify(localCategoriesDataSource, times(1)).getCategories()
        Assert.assertArrayEquals(categories.map { it.toDomain() }.toTypedArray(), result.toTypedArray())
    }

    @Test
    fun test_getCategories_byCategoryType_function_invokes_local_data_source_getCategories_byCategoryType_function() = runTest {
        val category1 = CategoryDataModel(1, "Title", Color.WHITE, type = CategoryType.PERSONAL.name, isMutable = true)
        val category2 = CategoryDataModel(2, "Title 2", Color.BLACK, type = CategoryType.PERSONAL.name, isMutable = true)
        val categories = arrayListOf(category1, category2)

        whenever(localCategoriesDataSource.getCategories(CategoryType.PERSONAL.name)).thenReturn(flowOf(categories))
        val result = SUT.getCategories(CategoryType.PERSONAL).first()
        val captor = argumentCaptor<String>()
        verify(localCategoriesDataSource, times(1)).getCategories(capture(captor))

        assertEquals(CategoryType.PERSONAL.name, captor.value)
        Assert.assertArrayEquals(categories.map { it.toDomain() }.toTypedArray(), result.toTypedArray())
    }

    @Test
    fun test_getCategories_byIds_function_invokes_local_data_source_getCategories_byIds_function() = runTest {

        val category1 = CategoryDataModel(1, "Title", Color.WHITE, type = CategoryType.PERSONAL.name, isMutable = true)
        val category2 = CategoryDataModel(2, "Title 2", Color.BLACK, type = CategoryType.PERSONAL.name, isMutable = true)
        val categories = arrayListOf(category1, category2)

        val arguments = listOf(1, 2)

        whenever(localCategoriesDataSource.getCategories(arguments)).thenReturn(flowOf(categories))
        val result = SUT.getCategories(arguments).first()
        val captor = argumentCaptor<List<Int>>()
        verify(localCategoriesDataSource, times(1)).getCategories(capture(captor))

        Assert.assertArrayEquals(arguments.toTypedArray(), captor.value.toTypedArray())
        Assert.assertArrayEquals(categories.map { it.toDomain() }.toTypedArray(), result.toTypedArray())
    }
}