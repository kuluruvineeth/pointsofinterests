package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.poi.models.PoiModel
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
class GetDetailedPoiUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT : GetDetailedPoiUseCase

    @Before
    fun setup(){
        SUT = GetDetailedPoiUseCase(repository)
    }


    @Test
    fun test_GetDetailedPoiUseCase_invokes_getDetailedPoi_repo_function() = runTest {
        val id = "someId"
        val model = mock<PoiModel>()
        whenever(repository.getDetailedPoi(anyNonNull())).thenReturn(model)
        val result = SUT.invoke(GetDetailedPoiUseCase.Params(id))
        val captor = argumentCaptor<String>()
        verify(repository,times(1)).getDetailedPoi(capture(captor))

        assertEquals(model, result)
        assertEquals(captor.value, id)
    }

    @Test
    fun test_GetDetailedPoiUseCase_throws_exception_when_getDetailedPoi_throws_exception() = runTest {

        val id = "someId"
        whenever(repository.getDetailedPoi(anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(GetDetailedPoiUseCase.Params(id))
    }
}