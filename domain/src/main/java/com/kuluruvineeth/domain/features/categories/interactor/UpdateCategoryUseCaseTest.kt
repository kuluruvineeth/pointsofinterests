package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.features.categories.models.Category
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class UpdateCategoryUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository

    private lateinit var SUT: UpdateCategoryUseCase

    @Before
    fun setup(){
        SUT = UpdateCategoryUseCase(repository)
    }

    @Test
    fun test_UpdateCategoryUseCaseTest_invokes_updateCategory_repo_function() =
        runTest {
            val category = mock<Category>()
            SUT.invoke(UpdateCategoryUseCase.Params(category))
            val captor = argumentCaptor<Category>()
            Mockito.verify(repository,Mockito.times(1)).updateCategory(capture(captor))
            assertEquals(captor.value,category)
        }

    @Test(expected = Throwable::class)
    fun test_UpdateCategoryUseCaseTest_throws_exception_when_updateCategory_throws_exception() = runTest {

        val category = mock<Category>()

        MockitoHelper.whenever(repository.updateCategory(MockitoHelper.anyNonNull())).thenThrow(
            IllegalStateException()
        )
        SUT.invoke(UpdateCategoryUseCase.Params(category))
    }
}