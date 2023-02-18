package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DeleteCommentUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: DeleteCommentUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = DeleteCommentUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test DeleteCommentUseCase invokes deleteComment repo function`() = runTest {
        val commentId = "123"
        SUT.invoke(DeleteCommentUseCase.Params(commentId))
        val idCaptor = argumentCaptor<String>()
        verify(repository, times(1)).deleteComment(capture(idCaptor))
        assertEquals(idCaptor.value, commentId)
    }

    @Test(expected = Throwable::class)
    fun `test DeleteCommentUseCase throws exception when deleteComment throws exception`() = runTest {
        val commentId = "123"
        whenever(repository.deleteComment(anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(DeleteCommentUseCase.Params(commentId))
    }
}