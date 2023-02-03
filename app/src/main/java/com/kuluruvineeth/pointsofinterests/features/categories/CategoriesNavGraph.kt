package com.kuluruvineeth.pointsofinterests.features.categories

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.kuluruvineeth.pointsofinterests.features.categories.ui.CategoriesDetailedScreen
import com.kuluruvineeth.pointsofinterests.features.categories.ui.CategoriesScreen
import com.kuluruvineeth.pointsofinterests.features.categories.viewmodel.CategoriesViewModel
import com.kuluruvineeth.pointsofinterests.features.main.PoiAppState
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import com.kuluruvineeth.pointsofinterests.navigation.Screen.CategoriesDetailed.ARG_CATEGORY_ID


fun NavGraphBuilder.categoriesGraph(appState: PoiAppState){

    navigation(startDestination = Screen.CategoriesList.route, route = Screen.Categories.route){
        composable(Screen.CategoriesList.route){
            val viewModel = hiltViewModel<CategoriesViewModel>()
            CategoriesScreen(
                appState,
                appState.snackBarHostState,
                appState.coroutineScope,
                viewModel
            )
        }
        composable(
            Screen.CategoriesDetailed.route,
            arguments = listOf(navArgument(ARG_CATEGORY_ID){
                type = NavType.StringType
                nullable = true
            })
        ){backStackEntry ->
            val parentEntry = remember(backStackEntry){
                appState.navHostController.getBackStackEntry(Screen.CategoriesList.route)
            }
            val parentViewModel = hiltViewModel<CategoriesViewModel>(parentEntry)
            val categoryId = backStackEntry.arguments?.getString(ARG_CATEGORY_ID)

            CategoriesDetailedScreen(categoryId = categoryId, parentViewModel){
                appState.onBackClick()
            }
        }
    }
}