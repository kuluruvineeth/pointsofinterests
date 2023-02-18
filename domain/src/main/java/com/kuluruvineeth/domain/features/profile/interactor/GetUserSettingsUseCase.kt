package com.kuluruvineeth.domain.features.profile.interactor

import com.kuluruvineeth.domain.core.FlowUseCase
import com.kuluruvineeth.domain.di.UseCaseDispatcher
import com.kuluruvineeth.domain.features.profile.model.UserSettings
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserSettingsUseCase @Inject constructor(
    private val repository: ProfileRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, UserSettings>(dispatcher) {
    override fun operation(params: Unit): Flow<UserSettings> =
        repository.getUserSetting()
}