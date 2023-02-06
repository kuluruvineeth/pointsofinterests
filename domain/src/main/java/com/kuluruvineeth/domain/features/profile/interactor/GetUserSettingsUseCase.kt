package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.core.FlowUseCase
import com.kuluruvineeth.domain.features.profile.module.UserSettings
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val repository: ProfileRepository
) : FlowUseCase<Unit,UserSettings>(){

    override fun operation(params: Unit) : Flow<UserSettings> =
        repository.getUserSetting()
}