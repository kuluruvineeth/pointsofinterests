package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class DeleteUserProfileUseCaseTest {


    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: DeleteUserProfileUseCase

    @Before
    fun setup(){
        SUT = DeleteUserProfileUseCase(repository)
    }

    @Test
    fun test_DeleteUserProfileUseCase_invokes_deleteUserProfile_repo_function() = runTest {
        SUT.invoke(Unit)
        Mockito.verify(repository,times(1)).deleteUserProfile()
    }

    @Test(expected = Throwable::class)
    fun test_DeleteUserProfileUseCase_throws_exception_when_deleteCategory_throws_exception() = runTest {

        MockitoHelper.whenever(repository.deleteUserProfile()).thenThrow(IllegalStateException())
        SUT.invoke(Unit)
    }
}