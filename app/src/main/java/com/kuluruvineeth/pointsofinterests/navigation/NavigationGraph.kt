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
import com.kuluruvineeth.pointsofinterests.features.profile.ui.ProfileScreen


@Composable
fun Navigation(
    navHostController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route,
        modifier = Modifier.padding(paddingValues)
    ){
        composable(Screen.Home.route){
            HomeScreen(navigationController = navHostController)
        }
        composable(Screen.Categories.route){
            CategoriesScreen(navHostController = navHostController)
        }
        composable(Screen.Profile.route){
            ProfileScreen(navHostController = navHostController)
        }
    }
}