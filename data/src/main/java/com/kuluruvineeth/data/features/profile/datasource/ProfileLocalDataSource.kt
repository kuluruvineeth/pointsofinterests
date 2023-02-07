package com.kuluruvineeth.data.features.profile.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import com.kuluruvineeth.data.core.UserProfile
import com.kuluruvineeth.data.core.UserSettings
import com.kuluruvineeth.data.features.profile.datastore.UserProfileProto
import com.kuluruvineeth.data.features.profile.datastore.UserSettingsProto
import com.kuluruvineeth.data.features.profile.model.UserProfileDataModel
import com.kuluruvineeth.data.features.profile.model.UserSettingsDataModel
import com.kuluruvineeth.data.features.profile.model.toDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject


class ProfileLocalDataSource @Inject constructor(
    @UserProfile private val userProfileDataStore: DataStore<UserProfileProto>,
    @UserSettings private val userSettingsDataStore: DataStore<UserSettingsProto>
) : ProfileDataSource {

    override fun getUserProfile(): Flow<UserProfileDataModel> =
        userProfileDataStore.data.map { it.toDataModel() }

    override suspend fun setUserProfile(userProfileDataModel: UserProfileDataModel) {
        try {
            userProfileDataStore.updateData {
                it.apply {
                    //TODO: Here UserProfileProto should be updated
                    /*UserProfileProto(

                    )*/
                }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to update user profile", ioException)
        }
    }

    override suspend fun deleteUserProfile() {
        try {
            userProfileDataStore.updateData {
                it.apply {
                    //UserProfileProto()
                }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to delete user profile", ioException)
        }
    }

    override fun getUserSettings(): Flow<UserSettingsDataModel> =
        userSettingsDataStore.data.map { it.toDataModel() }


    override suspend fun setShowOnBoarding(state: Boolean) {
        try {
            userSettingsDataStore.updateData {
                it.apply { setShowOnBoarding(state.not()) }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to setShowOnBoarding", ioException)
        }
    }

    override suspend fun setUseSystemTheme(state: Boolean) {
        try {
            userSettingsDataStore.updateData {
                it.apply { setUseSystemTheme(state.not()) }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to setUseSystemTheme", ioException)
        }
    }

    override suspend fun setUseDarkTheme(state: Boolean) {
        try {
            userSettingsDataStore.updateData {
                it.apply { setUseSystemTheme(state) }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to setUseDarkTheme", ioException)
        }
    }

    override suspend fun setUseAutoGc(state: Boolean) {
        try {
            userSettingsDataStore.updateData {
                it.apply { setUseDarkTheme(state) }
            }
        } catch (ioException: IOException) {
            Log.e(this::class.java.simpleName, "Failed to setUseAutoGc", ioException)
        }
    }

}