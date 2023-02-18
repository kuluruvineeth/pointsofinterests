package com.kuluruvineeth.data.profile.datasource

import com.kuluruvineeth.data.core.Local
import com.kuluruvineeth.data.features.profile.datasource.ProfileDataSource
import com.kuluruvineeth.data.features.profile.model.UserProfileDataModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class ProfileDataSourceInstrumentedTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Local
    lateinit var SUT: ProfileDataSource

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_getUserProfile_returns_empty_profile() = runTest {
        val profile = SUT.getUserProfile().first()
        assertEquals("", profile.token)
        assertEquals("", profile.email)
        assertEquals("", profile.name)
        assertEquals("", profile.profileImage)
    }

    @Test
    fun test_setProfile_and_getUserProfile_returns_consistent_data() = runTest {
        val testProfile = UserProfileDataModel(token = "Token", name = "Name", email = "email", profileImage = "imageUrl")
        SUT.setUserProfile(testProfile)

        val profile = SUT.getUserProfile().first()
        assertEquals(testProfile, profile)
    }

    @Test
    fun test_deleteUserProfile_returns_empty_profile() = runTest {
        val testProfile = UserProfileDataModel(token = "Token", name = "Name", email = "email", profileImage = "imageUrl")
        SUT.setUserProfile(testProfile)
        SUT.deleteUserProfile()

        val profile = SUT.getUserProfile().first()
        assertEquals("", profile.token)
        assertEquals("", profile.email)
        assertEquals("", profile.name)
        assertEquals("", profile.profileImage)
    }

    @Test
    fun test_getUserSettings_returns_default_values() = runTest {
        val userSettings = SUT.getUserSettings().first()

        assertTrue(userSettings.useSystemTheme)
        assertTrue(userSettings.showOnBoarding)

        assertFalse(userSettings.useDarkTheme)
        assertFalse(userSettings.useAutoGc)
    }

    @Test
    fun test_setShowOnBoarding_updates_value() = runTest {
        val userSettings = SUT.getUserSettings().first()
        assertTrue(userSettings.showOnBoarding)

        SUT.setShowOnBoarding(false)
        val userSettingsUpdated = SUT.getUserSettings().first()
        assertFalse(userSettingsUpdated.showOnBoarding)
    }

    @Test
    fun test_setUseSystemTheme_updates_value() = runTest {
        val userSettings = SUT.getUserSettings().first()
        assertTrue(userSettings.useSystemTheme)

        SUT.setUseSystemTheme(false)
        val userSettingsUpdated = SUT.getUserSettings().first()
        assertFalse(userSettingsUpdated.useSystemTheme)
    }

    @Test
    fun test_setUseAutoGc_updates_value() = runTest {
        val userSettings = SUT.getUserSettings().first()
        assertFalse(userSettings.useAutoGc)

        SUT.setUseAutoGc(true)
        val userSettingsUpdated = SUT.getUserSettings().first()
        assertTrue(userSettingsUpdated.useAutoGc)
    }

    @Test
    fun test_setUseDarkTheme_updates_value() = runTest {
        val userSettings = SUT.getUserSettings().first()
        assertFalse(userSettings.useDarkTheme)

        SUT.setUseDarkTheme(true)
        val userSettingsUpdated = SUT.getUserSettings().first()
        assertTrue(userSettingsUpdated.useDarkTheme)
    }
}