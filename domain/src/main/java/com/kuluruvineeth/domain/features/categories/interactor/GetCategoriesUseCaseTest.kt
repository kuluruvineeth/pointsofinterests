package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
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
class GetCategoriesUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository

    private lateinit var SUT: GetCategoriesUseCase

    @Before
    fun setup(){
        SUT = GetCategoriesUseCase(repository)
    }

    @Test
    fun test_GetCategoriesUseCaseTest_invokes_getCategories_repo_function() =
        runTest {
            val mockCategory1 = mock<Category>()
            val mockCategory2 = mock<Category>()
            val mockResult = arrayListOf(mockCategory1,mockCategory2)
            whenever(repository.getCategories()).thenReturn(flowOf(mockResult))
            val result = SUT.invoke(Unit).first()
            Mockito.verify(repository,Mockito.times(1)).getCategories()
            Assert.assertArrayEquals(mockResult.toTypedArray(),result!!.toTypedArray())
        }

    @Test
    fun test_GetCategoriesUseCaseTest_emits_exception_when_flow_in_getCategories_throws_exception() = runTest {
        whenever(repository.getCategories()).thenReturn(flow { throw IllegalStateException() })
        var excepton: Throwable = mock()
        SUT.invoke(Unit).catch { excepton = it }.toList()
        assertEquals(excepton::class.java, IllegalStateException::class.java)
    }
}