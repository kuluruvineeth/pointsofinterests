package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.poi.models.PoiComment
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
class GetCommentsUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: GetCommentsUseCase

    @Before
    fun setup(){
        SUT = GetCommentsUseCase(repository)
    }

    @Test
    fun test_GetCommentsUseCase_invokes_getComments_repo_function() = runTest {
        val targetId = "123"
        val comment1 = mock<PoiComment>()
        val comment2 = mock<PoiComment>()
        val mockResult = arrayListOf(comment1,comment2)

        whenever(repository.getComments(anyNonNull())).thenReturn(flowOf(mockResult))
        val result = SUT.invoke(GetCommentsUseCase.Params(targetId)).first()
        val captor = argumentCaptor<String>()
        verify(repository,times(1)).getComments(capture(captor))
        Assert.assertArrayEquals(mockResult.toTypedArray(), result!!.toTypedArray())
    }

    @Test
    fun test_GetCommentsUseCase_emits_exception_when_flow_in_getComments_throws_exception() = runTest {
        val targetId = "123"
        whenever(repository.getComments(anyNonNull())).thenReturn(flow {
            throw IllegalStateException()
        })
        var exception: Throwable = mock()
        SUT.invoke(GetCommentsUseCase.Params(targetId)).catch { exception = it }.toList()
        assertEquals(exception::class.java,IllegalStateException::class.java)
    }
}