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
import com.kuluruvineeth.pointsofinterests.navigation.routeToScreen
import com.kuluruvineeth.pointsofinterests.ui.theme.DarkMainColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    navHostController: NavHostController
) {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestinaton = navBackStackEntry?.destination
    val screenTitle = routeToScreen(currentDestinaton?.route)?.name?.let { stringResource(id = it) }
        ?: title
    TopAppBar(
        title = {
            Text(text = screenTitle, color = DarkMainColor)
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = DarkMainColor,
        navigationIcon = {
            if(navHostController.previousBackStackEntry != null){
                IconButton(
                    onClick = {
                        navHostController.navigateUp()
                    }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkMainColor
                    )
                }
            }
        }
    )
}