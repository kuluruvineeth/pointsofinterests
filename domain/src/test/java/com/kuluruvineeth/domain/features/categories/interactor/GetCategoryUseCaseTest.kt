package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
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
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetCategoryUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository

    private lateinit var SUT: GetCategoryUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = GetCategoryUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test GetCategoryUseCaseTest invokes getCategory repo function`() = runTest {
        val id = "someId"
        SUT.invoke(GetCategoryUseCase.Params(id))
        val captor = argumentCaptor<String>()
        Mockito.verify(repository, Mockito.times(1)).getCategory(capture(captor))
        assertEquals(captor.value, id)
    }

    @Test
    fun `test GetCategoryUseCaseTest returns same value as getCategory repo function`() = runTest {
        val id = "someId"
        val category = mock<Category>()
        whenever(repository.getCategory(anyNonNull())).thenReturn(category)
        val result = SUT.invoke(GetCategoryUseCase.Params(id))
        assertEquals(category, result)
    }

    @Test(expected = Throwable::class)
    fun `test GetCategoryUseCaseTest throws exception when getCategory throws exception`() = runTest {
        val id = "someId"
        whenever(repository.getCategory(anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(GetCategoryUseCase.Params(id))
    }
}