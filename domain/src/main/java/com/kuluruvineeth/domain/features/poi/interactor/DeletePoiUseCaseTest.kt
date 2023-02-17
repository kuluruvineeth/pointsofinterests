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
class DeletePoiUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: DeletePoiUseCase

    @Before
    fun setup(){
        SUT = DeletePoiUseCase(repository)
    }

    @Test
    fun test_DeletePoiUseCase_invokes_deletePoi_repo_function() = runTest {
        val model = mock<PoiModel>()
        SUT.invoke(DeletePoiUseCase.Params(model))
        val captor = argumentCaptor<PoiModel>()
        verify(repository, times(1)).deletePoi(capture(captor))
        assertEquals(captor.value,model)
    }

    @Test(expected = Throwable::class)
    fun test_DeletePoiUseCase_throws_exception_when_deletePoi_throws_exception() = runTest {

        val model = mock<PoiModel>()
        whenever(repository.deletePoi(anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(DeletePoiUseCase.Params(model))
    }
}