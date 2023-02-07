package com.kuluruvineeth.data.features.profile.repository

import com.kuluruvineeth.data.core.Local
import com.kuluruvineeth.data.features.profile.datasource.ProfileDataSource
import com.kuluruvineeth.data.features.profile.model.toDataModel
import com.kuluruvineeth.data.features.profile.model.toDomain
import com.kuluruvineeth.domain.features.profile.module.UserProfile
import com.kuluruvineeth.domain.features.profile.module.UserSettings
import com.kuluruvineeth.domain.features.profile.repo.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ProfileRepositoryImpl @Inject constructor(
    @Local private val dataSource: ProfileDataSource
) : ProfileRepository {

    override fun getUserProfile(): Flow<UserProfile> = dataSource.getUserProfile().map { it.toDomain() }

    override suspend fun setUserProfile(userProfile: UserProfile) {
        dataSource.setUserProfile(userProfile.toDataModel())
    }

    override suspend fun deleteUserProfile() {
        dataSource.deleteUserProfile()
    }

    override fun getUserSetting(): Flow<UserSettings> =
        dataSource.getUserSettings().map { it.toDomain() }

    override suspend fun setUseSystemTheme(state: Boolean) {
        dataSource.setUseSystemTheme(state)
    }

    override suspend fun setUseDarkTheme(state: Boolean) {
        dataSource.setUseDarkTheme(state)
    }

    override suspend fun setUseAutoGc(state: Boolean) {
        dataSource.setUseAutoGc(state)
    }

    override suspend fun setShowOnBoarding(state: Boolean) {
        dataSource.setShowOnBoarding(state)
    }
}