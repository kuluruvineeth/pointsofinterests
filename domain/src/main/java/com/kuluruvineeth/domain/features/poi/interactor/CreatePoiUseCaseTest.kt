package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.poi.models.PoiCreationPayload
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
class CreatePoiUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: CreatePoiUseCase

    @Before
    fun setup(){
        SUT = CreatePoiUseCase(repository)
    }

    @Test
    fun test_CreatePoiUseCase_invokes_createPoi_repo_function() = runTest {
        val payload = PoiCreationPayload("link","title","body","imageUrl", emptyList())
        SUT.invoke(CreatePoiUseCase.Params(payload))
        val captor = argumentCaptor<PoiCreationPayload>()
        verify(repository,times(1)).createPoi(capture(captor))
        assertEquals(captor.value,payload)
    }


    @Test(expected = Throwable::class)
    fun test_CreatePoiUseCase_throws_exception_when_createPoi_throws_exception() = runTest {

        val payload = PoiCreationPayload("link","title","body","imageUrl", emptyList())

        whenever(repository.createPoi(MockitoHelper.anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(CreatePoiUseCase.Params(payload))
    }
}