package com.kuluruvineeth.pointsofinterests.features.profile.ui

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.kuluruvineeth.pointsofinterests.features.profile.models.ProfileSectionItem
import com.kuluruvineeth.pointsofinterests.features.profile.models.ProfileSectionType
import com.kuluruvineeth.pointsofinterests.features.profile.models.UserInfo
import com.kuluruvineeth.pointsofinterests.features.profile.viewmodel.ProfileVm
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.PrimaryButton
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.PulsingProgressBar

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    vm: ProfileVm = viewModel()
) {
    val profileSectionsState by vm.profileScreenUiState.collectAsState()
    val context = LocalContext.current

    val onNavigate: (ProfileSectionType) -> Unit = {type ->
        if(type == ProfileSectionType.CATEGORIES){
            navHostController.navigate(Screen.Categories.route)
        }else{
            Toast.makeText(context, "This feature is under development",Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn{
        profileSectionsState.forEach { section ->
            item(section.type){
                if(section is ProfileSectionItem.AccountSectionItem){
                    AccountSection(
                        userInfo = section.userInfo,
                        vm::onSignInClicked
                    )
                }
                if(section is ProfileSectionItem.NavigationItem){
                    NavigationSection(
                        item = section,
                        onNavigationClicked = onNavigate
                    )
                }
                if(section is ProfileSectionItem.BooleanSettingsItem){
                    BooleanSettingsSection(
                        item = section,
                        onToggleBooleanSettings = vm::onSettingsToggled
                    )
                }
            }
            item {
                Divider(
                    Modifier
                        .height(0.5.dp)
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
                )
            }
        }
    }
}

@Composable
fun BooleanSettingsSection(
    item: ProfileSectionItem.BooleanSettingsItem,
    onToggleBooleanSettings: (ProfileSectionType) -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background, shape = RectangleShape)
            .clickable(item.isEnabled) {
                onToggleBooleanSettings(item.type)
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        BaseSettingContent(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            icon = item.icon,
            title = item.title,
            subtitle = item.subtitle,
            isEnabled = item.isEnabled
        )

        Switch(
            modifier = Modifier.padding(end = 16.dp),
            checked = item.state,
            onCheckedChange ={},
            enabled = item.isEnabled,
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.secondary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                uncheckedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                uncheckedThumbColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun NavigationSection(
    item: ProfileSectionItem.NavigationItem,
    onNavigationClicked: (ProfileSectionType) -> Unit
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background, shape = RectangleShape)
            .clickable(item.isEnabled) {
                onNavigationClicked(item.type)
            }
    ){
        BaseSettingContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            icon = item.icon,
            title = item.title,
            subtitle = item.subtitle,
            isEnabled = item.isEnabled
        )
    }
}

@Composable
fun AccountSection(
    userInfo: UserInfo?,
    onSignInClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.Center
    ){
        if(userInfo == null){
            PrimaryButton(
                Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.button_authorize),
                onClick = { onSignInClicked() }
            )
        }else{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    model = userInfo.avatarUrl,
                    contentDescription = "User image",
                    loading = { PulsingProgressBar()},
                    error = {
                        Icon(
                            modifier = Modifier.size(80.dp),
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_account_placeholder),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                ) 
                Spacer(modifier = Modifier.size(24.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = userInfo.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = userInfo.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun BaseSettingContent(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes title: Int,
    @StringRes subtitle: Int?,
    isEnabled: Boolean
) {
    val contentColor = MaterialTheme.colorScheme.onBackground.copy(
        alpha = if(isEnabled) 1f else 0.2f
    )
    Row(
        modifier = modifier
            .height(80.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = ImageVector.vectorResource(id = icon),
            contentDescription = "",
            tint = contentColor
        )
        Spacer(modifier = Modifier.size(16.dp))

        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.titleSmall,
                color = contentColor
            )

            if(subtitle != null){
                Text(
                    text = stringResource(id = subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = if (isEnabled) 0.5f else 0.1f)
                )
            }
        }
    }
}