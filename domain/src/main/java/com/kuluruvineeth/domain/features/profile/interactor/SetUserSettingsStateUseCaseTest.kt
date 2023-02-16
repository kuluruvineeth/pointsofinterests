package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.features.profile.module.ManualSettings
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random
import kotlin.test.assertEquals


@RunWith(MockitoJUnitRunner::class)
class SetUserSettingsStateUseCaseTest {

    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: SetUserSettingStateUseCase

    @Before
    fun setup(){
        SUT = SetUserSettingStateUseCase(repository)
    }

    @Test
    fun test_SetUserSettingStateUseCase_invokes_setUseSystemTheme_repo_function_when_ManualSettings_is_set_to_UseSystemTheme() =
        runTest {
            val manualSettings = ManualSettings.UseSystemTheme
            val state = Random.nextBoolean()
            SUT.invoke(SetUserSettingStateUseCase.Params(manualSettings,state))
            val captor = argumentCaptor<Boolean>()
            verify(repository,times(1)).setUseSystemTheme(capture(captor))
            assertEquals(captor.value, state)
        }

    @Test
    fun test_SetUserSettingsStateUseCase_invokes_setUseDarkTheme_repo_function_when_ManualSettings_is_set_to_UseDarkTheme() = runTest {

        val manualSettings = ManualSettings.UseDarkTheme
        val state = Random.nextBoolean()
        SUT.invoke(SetUserSettingStateUseCase.Params(manualSettings,state))
        val captor = argumentCaptor<Boolean>()
        verify(repository, times(1)).setUseSystemTheme(capture(captor))
        assertEquals(captor.value,state)
    }

    @Test
    fun test_SetUserSettingStateUseCase_invokes_setUseAutoGc_repo_function_when_ManualSettings_is_set_to_UseAutoGc() = runTest {

        val manualSettings = ManualSettings.UseAutoGc
        val state = Random.nextBoolean()
        SUT.invoke(SetUserSettingStateUseCase.Params(manualSettings, state))
        val captor = argumentCaptor<Boolean>()
        verify(repository, times(1)).setUseAutoGc(capture(captor))
        assertEquals(captor.value,state)
    }

    @Test(expected = Throwable::class)
    fun test_SetUserSettingStateUseCase_throws_exception_when_repo_function_throws_exception() = runTest {

        val manualSettings = ManualSettings.UseSystemTheme
        val state = Random.nextBoolean()

        MockitoHelper.whenever(repository.setUseSystemTheme(MockitoHelper.anyNonNull())).thenThrow(
            IllegalStateException()
        )
        SUT.invoke(SetUserSettingStateUseCase.Params(manualSettings,state))
    }
}