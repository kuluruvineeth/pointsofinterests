package com.kuluruvineeth.pointsofinterests.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.pointsofinterests.features.profile.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileVm @Inject constructor(

) : ViewModel(){

    val profileScreenUiState = collectProfileSections()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onSettingsToggled(type: ProfileSectionType){}

    fun onSignInClicked(){}

    private fun collectProfileSections(): Flow<List<ProfileSectionItem>> = flow {
        emit(createMockProfileItems())
    }

    private fun createMockProfileItems(): List<ProfileSectionItem> =
        arrayListOf<ProfileSectionItem>().apply {
            ProfileSectionType.values().forEach {
                val title = it.toTitle()
                val subtitle = it.toSubTitle()
                val icon = it.toIcon()
                this += when(it){
                    ProfileSectionType.CATEGORIES,
                    ProfileSectionType.ABOUT,
                    ProfileSectionType.STATISTICS ->
                        ProfileSectionItem.NavigationItem(icon,title,subtitle,true,it)

                    ProfileSectionType.GARBAGE_COLLECTOR,
                    ProfileSectionType.SYSTEM_THEME,
                    ProfileSectionType.DARK_THEME ->
                        ProfileSectionItem.BooleanSettingsItem(icon,title,subtitle,false,true,it)

                    ProfileSectionType.ACCOUNT -> ProfileSectionItem.AccountSectionItem(
                        UserInfo(
                            avatarUrl = "https://api.duniagames.co.id/api/content/upload/file/8143860661599124172.jpg",
                            fullName = "Kuluru Vineeth Kumar Reddy",
                            email = "kuluruvineeth8623@gmail.com"
                        )
                    )
                }
            }
        }
}