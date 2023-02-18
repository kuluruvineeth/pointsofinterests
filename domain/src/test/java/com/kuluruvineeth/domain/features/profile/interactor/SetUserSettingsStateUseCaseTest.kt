package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.MockitoHelper
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.features.profile.model.ManualSettings
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
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
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SetUserSettingsStateUseCaseTest {

    @Mock
    private lateinit var repository: ProfileRepository

    private lateinit var SUT: SetUserSettingStateUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        SUT = SetUserSettingStateUseCase(repository, UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test SetUserSettingStateUseCase invokes setUseSystemTheme repo function when ManualSettings is set to UseSystemTheme`() =
        runTest {
            val manualSettings = ManualSettings.UseSystemTheme
            val state = Random.nextBoolean()
            SUT.invoke(SetUserSettingStateUseCase.Params(manualSettings, state))
            val captor = argumentCaptor<Boolean>()
            verify(repository, times(1)).setUseSystemTheme(capture(captor))
            assertEquals(captor.value, state)
        }

    @Test
    fun `test SetUserSettingStateUseCase invokes setUseDarkTheme repo function when ManualSettings is set to UseDarkTheme`() =
        runTest {
            val manualSettings = ManualSettings.UseDarkTheme
            val state = Random.nextBoolean()
            SUT.invoke(SetUserSettingStateUseCase.Params(manualSettings, state))
            val captor = argumentCaptor<Boolean>()
            verify(repository, times(1)).setUseDarkTheme(capture(captor))
            assertEquals(captor.value, state)
        }

    @Test
    fun `test SetUserSettingStateUseCase invokes setUseAutoGc repo function when ManualSettings is set to UseAutoGc`() =
        runTest {
            val manualSettings = ManualSettings.UseAutoGc
            val state = Random.nextBoolean()
            SUT.invoke(SetUserSettingStateUseCase.Params(manualSettings, state))
            val captor = argumentCaptor<Boolean>()
            verify(repository, times(1)).setUseAutoGc(capture(captor))
            assertEquals(captor.value, state)
        }

    @Test(expected = Throwable::class)
    fun `test SetUserSettingStateUseCase throws exception when repo function throws exception`() = runTest {
        val manualSettings = ManualSettings.UseSystemTheme
        val state = Random.nextBoolean()
        MockitoHelper.whenever(repository.setUseSystemTheme(MockitoHelper.anyNonNull())).thenThrow(IllegalStateException())
        SUT.invoke(SetUserSettingStateUseCase.Params(manualSettings, state))
    }
}