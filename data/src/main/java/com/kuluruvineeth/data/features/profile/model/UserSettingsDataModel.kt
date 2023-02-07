package com.kuluruvineeth.data.features.profile.model

import com.kuluruvineeth.data.features.profile.datastore.UserSettingsProto
import com.kuluruvineeth.domain.features.profile.module.UserSettings

data class UserSettingsDataModel(
    val useSystemTheme: Boolean,
    val useDarkTheme: Boolean,
    val useAutoGc: Boolean,
    val showOnBoarding: Boolean
)

fun UserSettingsProto.toDataModel() = UserSettingsDataModel(
    useSystemTheme = useCustomTheme.not(),
    useDarkTheme = useDarkTheme,
    useAutoGc = useAutoGc,
    showOnBoarding = onboardingWasShown.not()
)

fun UserSettingsDataModel.toDomain() = UserSettings(
    isUseSystemTheme = useSystemTheme,
    isDarkMode = useDarkTheme,
    isAutoGcEnabled = useAutoGc,
    isShowOnBoarding = showOnBoarding
)