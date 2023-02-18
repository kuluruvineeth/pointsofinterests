package com.kuluruvineeth.pointofinterest.navigation

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.kuluruvineeth.pointofinterest.features.about.AboutScreen
import com.kuluruvineeth.pointofinterest.features.categories.categoriesGraph
import com.kuluruvineeth.pointofinterest.features.profile.ui.ProfileScreen
import com.kuluruvineeth.pointofinterest.features.home.ui.HomeScreen
import com.kuluruvineeth.pointofinterest.features.main.PoiAppState
import com.kuluruvineeth.pointofinterest.features.poi.create.ui.CreatePoiScreen
import com.kuluruvineeth.pointofinterest.features.poi.view.ui.ViewPoiScreen
import com.kuluruvineeth.pointofinterest.features.search.ui.SearchScreen

@Composable
fun Navigation(appState: PoiAppState, paddingValues: PaddingValues) {
    NavHost(appState.navController, startDestination = Screen.Home.route, Modifier.padding(paddingValues)) {
        composable(Screen.Home.route) {
            HomeScreen(
                showSortDialogState = appState.showSortDialog,
                onCloseSortDialog = { appState.showSortDialog = false },
                onNavigate = { screen, args -> appState.navigateTo(screen, args) }
            )
        }
        categoriesGraph(appState)
        composable(Screen.Search.route) { SearchScreen(appState) }
        composable(Screen.Profile.route) { ProfileScreen { appState.navigateTo(it) } }
        composable(Screen.About.route) { AboutScreen() }
        composable(
            Screen.CreatePoi.route,
            deepLinks = listOf(
                navDeepLink {
                    action = Intent.ACTION_SEND
                    mimeType = "text/*"
                },
                navDeepLink {
                    action = Intent.ACTION_SEND
                    mimeType = "image/*"
                }
            )
        ) {
            CreatePoiScreen(onCloseScreen = appState::onBackClick)
        }
        composable(
            Screen.ViewPoiDetailed.route,
            arguments = listOf(navArgument(Screen.ViewPoiDetailed.ARG_POI_ID) {
                type = NavType.StringType
                nullable = false
            })
        ) {
            ViewPoiScreen(appState, onCloseScreen = appState::onBackClick)
        }
    }
}