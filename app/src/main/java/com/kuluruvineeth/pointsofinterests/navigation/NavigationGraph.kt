package com.kuluruvineeth.pointsofinterests.navigation

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.kuluruvineeth.pointsofinterests.features.about.AboutScreen
import com.kuluruvineeth.pointsofinterests.features.categories.categoriesGraph
import com.kuluruvineeth.pointsofinterests.features.categories.ui.CategoriesScreen
import com.kuluruvineeth.pointsofinterests.features.home.ui.HomeScreen
import com.kuluruvineeth.pointsofinterests.features.main.PoiAppState
import com.kuluruvineeth.pointsofinterests.features.poi.create.CreatePoiScreen
import com.kuluruvineeth.pointsofinterests.features.poi.view.ViewPoiScreen
import com.kuluruvineeth.pointsofinterests.features.profile.ui.ProfileScreen
import com.kuluruvineeth.pointsofinterests.features.search.ui.SearchScreen


@RequiresApi(Build.VERSION_CODES.O)
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
                showSortDialogState = appState.showSortDialog,
                onCloseSortDialog = {appState.showSortDialog = false},
                onNavigate = {screen, args -> appState.navigateTo(screen,args)}
            )
        }
        categoriesGraph(appState)
        composable(Screen.Search.route){
            SearchScreen(appState = appState, searchQuery = appState.searchState.value )
        }
        composable(Screen.Profile.route){
            ProfileScreen{
                appState.navigateTo(it)
            }
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
            CreatePoiScreen(onCloseScreen = appState::onBackClick)
        }
        composable(
            Screen.ViewPoiDetailed.route,
            arguments = listOf(navArgument(Screen.ViewPoiDetailed.ARG_POI_ID){
                type = NavType.StringType
                nullable = false
            })
        ){
            
            ViewPoiScreen(appState, onCloseScreen = appState::onBackClick)
        }
    }
}