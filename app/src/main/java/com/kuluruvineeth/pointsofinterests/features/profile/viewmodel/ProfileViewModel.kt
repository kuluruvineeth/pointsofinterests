package com.kuluruvineeth.pointsofinterests.features.profile.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.kuluruvineeth.domain.features.profile.interactor.DeleteUserProfileUseCase
import com.kuluruvineeth.domain.features.profile.interactor.GetProfileUseCase
import com.kuluruvineeth.domain.features.profile.interactor.SetUserProfileUseCase
import com.kuluruvineeth.domain.features.profile.interactor.SetUserSettingStateUseCase
import com.kuluruvineeth.domain.features.profile.module.ManualSettings
import com.kuluruvineeth.domain.features.profile.module.Profile
import com.kuluruvineeth.domain.features.profile.module.UserProfile
import com.kuluruvineeth.pointsofinterests.features.profile.models.*
import com.kuluruvineeth.pointsofinterests.garbagecollector.GCWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    getProfileUseCase: GetProfileUseCase,
    private val setUserSettingStateUseCase: SetUserSettingStateUseCase,
    private val setUserProfileUseCase: SetUserProfileUseCase,
    private val deleteUserProfileUseCase: DeleteUserProfileUseCase,
    private val workManager: WorkManager
) : ViewModel(){

    val profileState = getProfileUseCase(Unit).map {
        it!!.toProfileUiModels()
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSettingsToggled(type: ProfileSectionType, currentState: Boolean){
        type.toManualSetting()?.let{setting ->
            viewModelScope.launch {
                setUserSettingStateUseCase(SetUserSettingStateUseCase.Params(
                    setting,
                    !currentState
                ))
                if(setting is ManualSettings.UseAutoGc)
                    onToggleGarbageCollector(currentState.not())
            }
        }
    }

    fun onUserSignedIn(task: Task<GoogleSignInAccount>){
        viewModelScope.launch {
            val account = task.result
            val userProfile = UserProfile(
                authToken = account.idToken,
                name = account.displayName,
                email = account.email,
                image = account.photoUrl.toString()
            )
            setUserProfileUseCase(SetUserProfileUseCase.Params(userProfile))
        }
    }

    fun onSignOutClicked(){
        viewModelScope.launch {
            deleteUserProfileUseCase(Unit)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onToggleGarbageCollector(newState: Boolean){
        if(newState){
            val gcWorkRequest = PeriodicWorkRequestBuilder<GCWorker>(Duration.ofDays(1))
                .setInitialDelay(Duration.ofDays(1))
                .addTag(GCWorker.TAG_GC)
                .build()
            workManager.enqueue(gcWorkRequest)
        }else{
            workManager.cancelAllWorkByTag(GCWorker.TAG_GC)
        }
    }

    private fun ProfileSectionType.toManualSetting() = when(this){
        ProfileSectionType.GARBAGE_COLLECTOR -> ManualSettings.UseAutoGc
        ProfileSectionType.SYSTEM_THEME -> ManualSettings.UseSystemTheme
        ProfileSectionType.DARK_THEME -> ManualSettings.UseDarkTheme
        else -> null
    }

    private fun Profile.toProfileUiModels(): List<ProfileSectionItem> =
        ProfileSectionType.values().filter{it !in wipeSections}.map {
            val title = it.toTitle()
            val subtitle = it.toSubTitle()
            val icon = it.toIcon()

            when(it){
                ProfileSectionType.CATEGORIES,
                ProfileSectionType.ABOUT,
                ProfileSectionType.STATISTICS ->
                    ProfileSectionItem.NavigationItem(icon,title,subtitle,true,it)
                ProfileSectionType.GARBAGE_COLLECTOR ->
                    ProfileSectionItem.BooleanSettingsItem(icon,title,subtitle,userSettings.isAutoGcEnabled,true,it)
                ProfileSectionType.SYSTEM_THEME ->
                    ProfileSectionItem.BooleanSettingsItem(icon,title,subtitle,userSettings.isUseSystemTheme,true,it)
                ProfileSectionType.DARK_THEME ->
                    ProfileSectionItem.BooleanSettingsItem(
                        icon,
                        title,
                        subtitle,
                        userSettings.isDarkMode,
                        userSettings.isUseSystemTheme.not(),
                        it
                    )
                ProfileSectionType.ACCOUNT -> ProfileSectionItem.AccountSectionItem(
                    userInfo = if(userProfile.isAuthorized)
                        UserInfo(
                            avatarUrl = userProfile.image,
                            fullName = requireNotNull(userProfile.name),
                            email = requireNotNull(userProfile.email)
                        )else null
                )
            }
        }
}