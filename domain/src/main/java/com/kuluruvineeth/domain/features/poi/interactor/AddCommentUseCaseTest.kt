package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.poi.models.PoiCommentPayload
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class AddCommentUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: AddCommentUseCase

    @Before
    fun setup(){
        SUT = AddCommentUseCase(repository)
    }

    @Test
    fun test_AddCommentUseCase_invokes_addComment_repo_function() = runTest {
        val targetPoiId = "123"
        val commentCreationPayload = PoiCommentPayload("message")
        SUT.invoke(AddCommentUseCase.Params(targetPoiId,commentCreationPayload))
        val idCaptor = argumentCaptor<String>()
        val payloadCaptor = argumentCaptor<PoiCommentPayload>()
        verify(repository, Mockito.times(1)).addComment(capture(idCaptor), capture(payloadCaptor))
        assertEquals(payloadCaptor.value,commentCreationPayload)
        assertEquals(idCaptor.value, targetPoiId)
    }

    @Test(expected = Throwable::class)
    fun test_AddCategoryUseCaseTest_throws_exception_when_addCategory_throws_exception() = runTest {
        val targetPoiId = "123"
        val commentCreationPayload = PoiCommentPayload("message")
        whenever(repository.addComment(anyNonNull(), anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(AddCommentUseCase.Params(targetPoiId, commentCreationPayload))
    }
}