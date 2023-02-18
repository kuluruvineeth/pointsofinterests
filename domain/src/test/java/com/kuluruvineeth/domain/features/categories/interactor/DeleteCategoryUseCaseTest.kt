package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
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
class DeleteCategoryUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository

    private lateinit var SUT: DeleteCategoryUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = DeleteCategoryUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test DeleteCategoryUseCaseTest invokes deleteCategory repo function`() = runTest {
        val id = "id"
        SUT.invoke(DeleteCategoryUseCase.Params(id))
        val captor = argumentCaptor<String>()
        Mockito.verify(repository, Mockito.times(1)).deleteCategory(capture(captor))
        assertEquals(captor.value, id)
    }

    @Test(expected = Throwable::class)
    fun `test DeleteCategoryUseCaseTest throws exception when deleteCategory throws exception`() = runTest {
        val id = "id"
        MockitoHelper.whenever(repository.deleteCategory(MockitoHelper.anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(DeleteCategoryUseCase.Params(id))
    }
}