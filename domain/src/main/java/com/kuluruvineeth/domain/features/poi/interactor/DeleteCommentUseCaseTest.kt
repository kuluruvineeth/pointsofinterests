package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class DeleteCommentUseCaseTest {


    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: DeleteCommentUseCase

    @Before
    fun setup(){
        SUT = DeleteCommentUseCase(repository)
    }

    @Test
    fun test_DeleteCommentUseCase_invokes_deleteComment_repo_function() = runTest {

        val commentId = "123"
        SUT.invoke(DeleteCommentUseCase.Params(commentId))
        val idCaptor = argumentCaptor<String>()
        verify(repository, times(1)).deleteComment(capture(idCaptor))
        assertEquals(idCaptor.value, commentId)
    }

    @Test
    fun test_DeleteCommentUseCase_throws_exception_when_deleteComment_throws_exception() = runTest {
        val commentId = "123"

        whenever(repository.deleteComment(anyNonNull())).thenThrow(IllegalStateException())

        SUT.invoke(DeleteCommentUseCase.Params(commentId))
    }
}