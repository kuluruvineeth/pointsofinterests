package com.kuluruvineeth.data.features.profile.repository

import com.kuluruvineeth.data.features.profile.datasource.ProfileDataSource
import com.kuluruvineeth.data.features.profile.model.UserProfileDataModel
import com.kuluruvineeth.data.features.profile.model.UserSettingsDataModel
import com.kuluruvineeth.data.features.profile.model.toDataModel
import com.kuluruvineeth.data.features.profile.model.toDomain
import com.kuluruvineeth.domain.MockitoHelper.anyNonNull
import com.kuluruvineeth.domain.MockitoHelper.argumentCaptor
import com.kuluruvineeth.domain.MockitoHelper.capture
import com.kuluruvineeth.domain.MockitoHelper.whenever
import com.kuluruvineeth.domain.features.profile.module.UserProfile
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
class ProfileRepositoryTest {

    @Mock
    private lateinit var localDataSource: ProfileDataSource

    private lateinit var SUT: ProfileRepository

    @Before
    fun setup() {
        SUT = ProfileRepositoryImpl(localDataSource)
    }

    @Test
    fun test_getUserProfile_function_invokes_local_data_source_getUserProfile() = runTest {

        val userProfileStub = UserProfileDataModel("token", "name", "email", "image")
        whenever(localDataSource.getUserProfile()).thenReturn(flowOf(userProfileStub))
        val result = SUT.getUserProfile().first()
        verify(localDataSource, times(1)).getUserProfile()
        assertEquals(userProfileStub.toDomain(), result)
    }

    @Test
    fun test_setUserProfile_function_invokes_local_data_source_setUserProfile() = runTest {

        val userProfileStub = UserProfile("token", "name", "email", "image")
        whenever(localDataSource.setUserProfile(anyNonNull())).thenReturn(Unit)
        SUT.setUserProfile(userProfileStub)
        val captor = argumentCaptor<UserProfileDataModel>()
        verify(localDataSource, times(1)).setUserProfile(capture(captor))
        assertEquals(userProfileStub.toDataModel(), captor.value)
    }

    @Test
    fun test_deleteUserProfile_function_invokes_local_data_source_deleteUserProfile() = runTest {

        whenever(localDataSource.deleteUserProfile()).thenReturn(Unit)
        SUT.deleteUserProfile()
        verify(localDataSource, times(1)).deleteUserProfile()
    }

    @Test
    fun test_getUserSettings_function_invokes_local_data_source_getUserSettings() = runTest {

        val userSettings = UserSettingsDataModel(useSystemTheme = true, useDarkTheme = false, useAutoGc = false, showOnBoarding = false)
        whenever(localDataSource.getUserSettings()).thenReturn(flowOf(userSettings))
        val result = SUT.getUserSetting().first()
        verify(localDataSource, times(1)).getUserSettings()
        assertEquals(userSettings.toDomain(), result)
    }

    @Test
    fun test_setUseSystemTheme_function_invokes_local_data_source_setUseSystemTheme() = runTest {

        val argument = Random.nextBoolean()
        whenever(localDataSource.setUseSystemTheme(anyNonNull())).thenReturn(Unit)
        SUT.setUseSystemTheme(argument)
        val captor = argumentCaptor<Boolean>()
        verify(localDataSource, times(1)).setUseSystemTheme(capture(captor))
        assertEquals(argument, captor.value)
    }

    @Test
    fun test_setUseDarkTheme_function_invokes_local_data_source_setUseDarkTheme() = runTest {

        val argument = Random.nextBoolean()
        whenever(localDataSource.setUseDarkTheme(anyNonNull())).thenReturn(Unit)
        SUT.setUseDarkTheme(argument)
        val captor = argumentCaptor<Boolean>()
        verify(localDataSource, times(1)).setUseDarkTheme(capture(captor))
        assertEquals(argument, captor.value)
    }

    @Test
    fun test_setUseAutoGc_function_invokes_local_data_source_setUseAutoGc() = runTest {

        val argument = Random.nextBoolean()
        whenever(localDataSource.setUseAutoGc(anyNonNull())).thenReturn(Unit)
        SUT.setUseAutoGc(argument)
        val captor = argumentCaptor<Boolean>()
        verify(localDataSource, times(1)).setUseAutoGc(capture(captor))
        assertEquals(argument, captor.value)
    }

    @Test
    fun test_setShowOnBoarding_function_invokes_local_data_source_setShowOnBoarding() = runTest {

        val argument = Random.nextBoolean()
        whenever(localDataSource.setShowOnBoarding(anyNonNull())).thenReturn(Unit)
        SUT.setShowOnBoarding(argument)
        val captor = argumentCaptor<Boolean>()
        verify(localDataSource, times(1)).setShowOnBoarding(capture(captor))
        assertEquals(argument, captor.value)
    }
}