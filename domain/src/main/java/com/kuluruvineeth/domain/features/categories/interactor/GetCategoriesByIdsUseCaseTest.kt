package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class GetCategoriesByIdsUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository

    private lateinit var SUT: GetCategoriesByIdsUseCase

    @Before
    fun setup(){
        SUT = GetCategoriesByIdsUseCase(repository)
    }

    @Test
    fun test_GetCategoriesByIdsUseCaseTest_invokes_getCategories_repo_function() = runTest {
        val ids = arrayListOf(1,2,3)

        val mockCategory1 = mock<Category>()
        val mockCategory2 = mock<Category>()
        val mockCategory3 = mock<Category>()

        val mockResult = arrayListOf(mockCategory1,mockCategory2,mockCategory3)

        MockitoHelper.whenever(repository.getCategories(ids)).thenReturn(flowOf(mockResult))

        val result = SUT.invoke(GetCategoriesByIdsUseCase.Params(ids)).first()
        val captor = argumentCaptor<List<Int>>()
        Mockito.verify(repository,Mockito.times(1)).getCategories(capture(captor))
        Assert.assertArrayEquals(ids.toTypedArray(),captor.value.toTypedArray())
        Assert.assertArrayEquals(mockResult.toTypedArray(),result!!.toTypedArray())
    }

    @Test
    fun test_GetCategoriesByIdsUseCaseTest_emits_exception_when_flow_in_getCategories_throws_exception() = runTest {
        val ids = arrayListOf(1,2,3)
        MockitoHelper.whenever(repository.getCategories(ids)).thenReturn(flow {
            throw IllegalStateException()
        })
        var exception: Throwable = mock()
        SUT.invoke(GetCategoriesByIdsUseCase.Params(ids)).catch { exception = it }.toList()
        assertEquals(IllegalStateException::class.java, exception::class.java)
    }
}