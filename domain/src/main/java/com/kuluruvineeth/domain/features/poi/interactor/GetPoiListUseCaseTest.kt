package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.poi.models.PoiModel
import com.kuluruvineeth.domain.features.poi.models.PoiSortOption
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
class GetPoiListUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: GetPoiListUseCase

    @Before
    fun setup(){
        SUT = GetPoiListUseCase(repository)
    }

    @Test
    fun test_GetPoiListUseCase_invokes_getPoiList_repo_function() = runTest {
        val sortOption = mock<PoiSortOption>()
        val model1 = mock<PoiModel>()
        val model2 = mock<PoiModel>()
        val mockResult = arrayListOf(model1,model2)

        whenever(repository.getPoiList(anyNonNull())).thenReturn(flowOf(mockResult))
        val result = SUT.invoke(GetPoiListUseCase.Params(sortOption)).first()
        val captor = argumentCaptor<PoiSortOption>()
        verify(repository, times(1)).getPoiList(capture(captor))

        assertEquals(sortOption, captor.value)
        Assert.assertArrayEquals(mockResult.toTypedArray(),result!!.toTypedArray())
    }

    @Test
    fun test_GetPoiListUseCase_emits_exception_when_flow_in_getPoiList_throws_exception() = runTest {

        val sortOption = mock<PoiSortOption>()
        whenever(repository.getPoiList(anyNonNull())).thenReturn(flow {
            throw IllegalStateException()
        })
        var exception : Throwable = mock()
        SUT.invoke(GetPoiListUseCase.Params(sortOption)).catch { exception = it }.toList()
        assertEquals(exception::class.java, IllegalStateException::class.java)
    }
}