package com.kuluruvineeth.pointsofinterests.features.main

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kuluruvineeth.pointsofinterests.navigation.MenuActionType
import com.kuluruvineeth.pointsofinterests.navigation.Screen
import com.kuluruvineeth.pointsofinterests.navigation.getMainScreens
import com.kuluruvineeth.pointsofinterests.navigation.routeToScreen
import kotlinx.coroutines.CoroutineScope


@Composable
fun rememberPoiAppState(
    navHostController: NavHostController = rememberNavController(),
    snackBarHostState: SnackbarHostState = remember {
        SnackbarHostState()
    },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    searchState: MutableState<TextFieldValue> = remember {
        mutableStateOf(TextFieldValue(""))
    }
): PoiAppState {

    return remember(navHostController,snackBarHostState,coroutineScope,searchState) {
        PoiAppState(
            navHostController,
            snackBarHostState,
            coroutineScope,
            searchState
        )
    }
}

class PoiAppState(
    val navHostController: NavHostController,
    val snackBarHostState: SnackbarHostState,
    val coroutineScope: CoroutineScope,
    val searchState: MutableState<TextFieldValue>
) {
    val navBackStackEntry: NavBackStackEntry?
        @Composable get() = navHostController.currentBackStackEntryAsState().value

    val currentDestination: NavDestination?
        @Composable get() = navBackStackEntry?.destination

    val currentScreen: Screen?
        @Composable get() = routeToScreen(currentDestination?.route)

    val isRootScreen: Boolean
        @Composable get() = currentScreen in getMainScreens()

    val isCurrentFullScreen: Boolean
     @Composable get() = currentScreen?.isFullScreen == true

    var showSearchBarState by mutableStateOf(false)

    var showSortDialog by mutableStateOf(false)

    fun navigateToRoot(screen: Screen){
        navHostController.navigate(screen.route){
            popUpTo(navHostController.graph.findStartDestination().id){
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateTo(screen: Screen, arguments: List<Pair<String, Any>> = emptyList()) {
        val route = if (arguments.isEmpty().not()) {
            screen.routePath + "?" + arguments.joinToString(separator = ",") { "${it.first}=${it.second}" }
        } else {
            screen.route
        }
        navHostController.navigate(route)
    }

    fun onBackClick(){
        navHostController.popBackStack()
    }

    fun onMenuItemClicked(actionType: MenuActionType){
        when(actionType){
            MenuActionType.BACK -> onBackClick()
            MenuActionType.SEARCH -> showSearchBarState = true
            MenuActionType.ADD -> navigateTo(Screen.CategoriesDetailed)
            MenuActionType.SORT -> showSortDialog = true
        }
    }

}