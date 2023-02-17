package com.kuluruvineeth.data.features.profile.model

import com.kuluruvineeth.data.features.profile.datastore.UserSettingsProto
import org.junit.Test
import kotlin.test.assertEquals

class UserSettingsDataModelTest {

    @Test
    fun test_UserSettingsDataModel_toDomain_function_returns_UserSettings_model_with_correct_fields(){

        val dataModel = UserSettingsDataModel(
            useSystemTheme = false,
            useDarkTheme = true,
            useAutoGc = false,
            showOnBoarding = false
        )

        val domainModel = dataModel.toDomain()

        assertEquals(dataModel.useSystemTheme, domainModel.isUseSystemTheme)
        assertEquals(dataModel.useDarkTheme, domainModel.isDarkMode)
        assertEquals(dataModel.useAutoGc, domainModel.isAutoGcEnabled)
        assertEquals(dataModel.showOnBoarding, domainModel.isShowOnBoarding)
    }

    @Test
    fun test_UserSettingsProto_toDataModel_function_returns_UserSettingsDataModel_model_with_correct_fields(){

        val userSettingsProto = UserSettingsProto.newBuilder().apply {
            useCustomTheme = false
            useDarkTheme = false
            useAutoGc = false
            onboardingWasShown = false
        }.build()

        val dataModel = userSettingsProto.toDataModel()

        assertEquals(dataModel.useSystemTheme, userSettingsProto.useCustomTheme.not())
        assertEquals(dataModel.useDarkTheme, userSettingsProto.useDarkTheme)
        assertEquals(dataModel.useAutoGc, userSettingsProto.useAutoGc)
        assertEquals(dataModel.showOnBoarding, userSettingsProto.onboardingWasShown.not())
    }
}