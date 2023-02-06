package com.kuluruvineeth.domain.features.profile.module

sealed class ManualSettings{

    object UseSystemTheme : ManualSettings()
    object UseDarkTheme: ManualSettings()
    object UseAutoGc: ManualSettings()
}
