package com.kuluruvineeth.domain.features.poi.interactor

import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.poi.models.PoiModel
import com.kuluruvineeth.domain.features.poi.repo.PoiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
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
class SearchPoiUseCaseTest {

    @Mock
    private lateinit var repository: PoiRepository

    private lateinit var SUT: SearchPoiUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = SearchPoiUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test SearchPoiUseCase invokes searchPoi repo function`() = runTest {
        val query = "Something"
        val searchResult1 = mock<PoiModel>()
        val searchResult2 = mock<PoiModel>()
        val searchResult3 = mock<PoiModel>()
        val mockResult = arrayListOf(searchResult1, searchResult2, searchResult3)

        whenever(repository.searchPoi(anyNonNull())).thenReturn(mockResult)

        val result = SUT.invoke(SearchPoiUseCase.Params(query))
        val captor = argumentCaptor<String>()
        verify(repository, times(1)).searchPoi(capture(captor))
        assertEquals(query, captor.value)
        Assert.assertArrayEquals(mockResult.toTypedArray(), result.toTypedArray())
    }

    @Test(expected = Throwable::class)
    fun `test SearchPoiUseCase throws exception when searchPoi throws exception`() = runTest {
        val query = "Something"
        whenever(repository.searchPoi(anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(SearchPoiUseCase.Params(query))
    }
}