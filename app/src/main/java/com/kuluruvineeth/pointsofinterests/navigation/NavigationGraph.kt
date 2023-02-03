package com.kuluruvineeth.pointsofinterests.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                appState.searchState
            )
        }
        composable(Screen.Categories.route){
            CategoriesScreen(
                navHostController = appState.navHostController,
                appState.snackBarHostState,
                appState.coroutineScope
            )
        }
        composable(Screen.Profile.route){
            ProfileScreen(navHostController = appState.navHostController)
        }
        composable(Screen.CreatePoi.route){
            CreatePoiScreen(navHostController = appState.navHostController)
        }
    }
}