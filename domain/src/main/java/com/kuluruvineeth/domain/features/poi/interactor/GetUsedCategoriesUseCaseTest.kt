package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.flow.*
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
class GetUsedCategoriesUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: GetUsedCategoriesUseCase

    @Before
    fun setup(){
        SUT = GetUsedCategoriesUseCase(repository)
    }

    @Test
    fun test_GetUsedCategoriesUseCase_invokes_getUsedCategories_repo_function() = runTest {

        val mockResult = arrayListOf(1,2,3)
        whenever(repository.getUsedCategories()).thenReturn(flowOf(mockResult))
        val result = SUT.invoke(Unit).first()
        verify(repository, times(1)).getUsedCategories()
        Assert.assertArrayEquals(mockResult.toTypedArray(), result!!.toTypedArray())
    }

    @Test
    fun test_GetUsedCategoriesUseCase_emits_exception_when_flow_in_getUsedCategories_throws_exception() = runTest {

        whenever(repository.getUsedCategories()).thenReturn(flow {
            throw IllegalStateException()
        })
        var exception: Throwable = mock()
        SUT.invoke(Unit).catch { exception = it }.toList()
        assertEquals(exception::class.java, IllegalStateException::class.java)
    }
}