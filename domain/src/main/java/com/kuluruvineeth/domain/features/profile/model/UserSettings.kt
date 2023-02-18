package com.kuluruvineeth.domain.features.profile.model

data class UserSettings(
    val isUseSystemTheme: Boolean,
    val isDarkMode: Boolean,
    val isAutoGcEnabled: Boolean,
    val isShowOnBoarding: Boolean
)