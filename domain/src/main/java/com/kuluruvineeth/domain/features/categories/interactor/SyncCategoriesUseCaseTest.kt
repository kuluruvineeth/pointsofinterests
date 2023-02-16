package com.kuluruvineeth.domain.features.categories.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.features.categories.repo.CategoriesRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SyncCategoriesUseCaseTest {

    @Mock
    private lateinit var repository: CategoriesRepository

    private lateinit var SUT: SyncCategoriesUseCase

    @Before
    fun setup(){
        SUT = SyncCategoriesUseCase(repository)
    }

    @Test
    fun test_SyncCategoriesUseCaseTest_invokes_sync_repo_function() = runTest {
        SUT.invoke(Unit)
        Mockito.verify(repository,Mockito.times(1)).sync()
    }

    @Test(expected = Throwable::class)
    fun test_SyncCategoriesUseCaseTest_throws_exception_when_sync_throws_exception() = runTest {
        MockitoHelper.whenever(repository.sync()).thenThrow(IllegalStateException())
        SUT.invoke(Unit)
    }
}