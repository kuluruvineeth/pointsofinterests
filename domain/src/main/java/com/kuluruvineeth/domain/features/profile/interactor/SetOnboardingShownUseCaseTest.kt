package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertFalse


@RunWith(MockitoJUnitRunner::class)
class SetOnboardingShownUseCaseTest {


    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: SetOnboardingShownUseCase

    @Before
    fun setup(){
        SUT = SetOnboardingShownUseCase(repository)
    }

    @Test
    fun test_SetOnboardingShownUseCaseTest_invokes_setShowOnBoarding_repo_function() = runTest {
        SUT.invoke(Unit)
        val captor = argumentCaptor<Boolean>()
        verify(repository, times(1)).setShowOnBoarding(capture(captor))
        assertFalse(captor.value)
    }

    @Test(expected = Throwable::class)
    fun test_SetOnboardingShownUseCaseTest_throws_exception_when_setShowOnBoarding_throws_exception() = runTest {

        MockitoHelper.whenever(repository.setShowOnBoarding(MockitoHelper.anyNonNull())).thenThrow(
            IllegalStateException()
        )
        SUT.invoke(Unit)
    }
}