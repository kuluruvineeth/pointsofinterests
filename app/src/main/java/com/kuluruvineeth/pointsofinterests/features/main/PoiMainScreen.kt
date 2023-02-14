package com.kuluruvineeth.pointsofinterests.features.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.navigation.Navigation
import com.kuluruvineeth.pointsofinterests.navigation.getMainScreens
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.BottomBar

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PoiMainScreen(
    appState: PoiAppState = rememberPoiAppState()
) {

        Scaffold(
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        snackbarHost = {
            androidx.compose.material3.SnackbarHost(hostState = appState.snackBarHostState){
                androidx.compose.material3.Snackbar(snackbarData = it, actionColor = MaterialTheme.colorScheme.secondary)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = appState.isCurrentFullScreen.not(),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        appState.navigateTo(Screen.CreatePoi)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        topBar = {
            com.kuluruvineeth.pointsofinterests.ui.composables.uikit.AppBar(
                title = stringResource(id = R.string.app_name),
                appState
            )
        },
        bottomBar = {
            if(appState.isCurrentFullScreen.not()){
                BottomBar(
                    appState,
                    items = getMainScreens()
                )
            }
        }
    ) {paddingValues ->
            Navigation(
                appState = appState,
                paddingValues = paddingValues
            )
    }
}
