package com.kuluruvineeth.pointsofinterests.ui.composables.uikit


import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import com.kuluruvineeth.pointsofinterests.navigation.getMainScreens
import com.kuluruvineeth.pointsofinterests.navigation.routeToScreen
import com.kuluruvineeth.pointsofinterests.ui.theme.DarkMainColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    currentScreen: Screen?,
    navHostController: NavHostController
) {
    val screenTitle = currentScreen?.name?.let { stringResource(id = it) } ?: title
    TopAppBar(
        title = {
            Text(text = screenTitle, color = MaterialTheme.colorScheme.onBackground)
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        navigationIcon = {
            if(navHostController.previousBackStackEntry != null && currentScreen !in getMainScreens()){
                IconButton(
                    onClick = {
                        navHostController.navigateUp()
                    }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    )
}