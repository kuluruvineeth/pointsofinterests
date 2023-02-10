package com.kuluruvineeth.pointsofinterests.navigation

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.kuluruvineeth.pointsofinterests.features.about.AboutScreen
import com.kuluruvineeth.pointsofinterests.features.categories.categoriesGraph
import com.kuluruvineeth.pointsofinterests.features.categories.ui.CategoriesScreen
import com.kuluruvineeth.pointsofinterests.features.home.ui.HomeScreen
import com.kuluruvineeth.pointsofinterests.features.main.PoiAppState
import com.kuluruvineeth.pointsofinterests.features.poi.create.CreatePoiScreen
import com.kuluruvineeth.pointsofinterests.features.profile.ui.ProfileScreen


@Composable
fun Navigation(
    appState: PoiAppState,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = appState.navHostController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ){
        composable(Screen.Home.route){
            HomeScreen(
                navigationController = appState.navHostController,
                appState.searchState,
                appState.showSortDialog,
                {appState.showSortDialog = false}
            )
        }
        categoriesGraph(appState)
        composable(Screen.Profile.route){
            ProfileScreen(navHostController = appState.navHostController)
        }
        composable(Screen.About.route){
            AboutScreen()
        }
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
        ){
            CreatePoiScreen(navHostController = appState.navHostController)
        }
    }
}