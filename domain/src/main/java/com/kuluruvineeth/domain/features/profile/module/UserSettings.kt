package com.kuluruvineeth.domain.features.profile.module

data class UserSettings(
    val isUseSystemTheme: Boolean,
    val isDarkMode: Boolean,
    val isAutoGcEnabled: Boolean,
    val isShownOnBoarding: Boolean
)
