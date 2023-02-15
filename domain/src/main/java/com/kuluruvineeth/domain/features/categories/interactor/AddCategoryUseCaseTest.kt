package com.kuluruvineeth.domain.features.categories.interactor

import android.graphics.Color
import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.categories.models.CreateCategoryPayload
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class AddCategoryUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository

    private lateinit var SUT: AddCategoryUseCase

    @Before
    fun setup(){
        SUT = AddCategoryUseCase(repository)
    }

    @Test
    fun test_add_category_use_case_invokes_addCategory_repo_function() =
        runTest{
            val categoryName = "Random name"
            val color = Color.WHITE
            SUT.invoke(AddCategoryUseCase.Params(categoryName,color))
            val payloadCapture = argumentCaptor<CreateCategoryPayload>()
            verify(repository, times(1)).addCategory(capture(payloadCapture))
            assertEquals(payloadCapture.value,CreateCategoryPayload(categoryName,color))

        }

    @Test(expected = Throwable::class)
    fun test_add_category_use_case_throws_exception_when_addCategory_throws_exception() = runTest {
        val categoryName = "Random name"
        val color = Color.WHITE

        whenever(repository.addCategory(anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(AddCategoryUseCase.Params(categoryName,color))
    }
}