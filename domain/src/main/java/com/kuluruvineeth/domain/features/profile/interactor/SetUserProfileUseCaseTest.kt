package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.mock
import com.kuluruvineeth.domain.features.profile.module.UserProfile
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class SetUserProfileUseCaseTest {

    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: SetUserProfileUseCase

    @Before
    fun setup(){
        SUT = SetUserProfileUseCase(repository)
    }

    @Test
    fun test_SetUserProfileUseCaseTest_invokes_setUserProfile_repo_function() = runTest {
        val userProfile = mock<UserProfile>()
        SUT.invoke(SetUserProfileUseCase.Params(userProfile))
        val captor = MockitoHelper.argumentCaptor<UserProfile>()
        Mockito.verify(repository, Mockito.times(1)).setUserProfile(capture(captor))
        assertEquals(captor.value, userProfile)
    }

    @Test(expected = Throwable::class)
    fun test_SetUserProfileUseCaseTest_throws_exception_when_addCategory_throws_exception() = runTest {

        val userProfile = mock<UserProfile>()
        MockitoHelper.whenever(repository.setUserProfile(MockitoHelper.anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(SetUserProfileUseCase.Params(userProfile))
    }
}