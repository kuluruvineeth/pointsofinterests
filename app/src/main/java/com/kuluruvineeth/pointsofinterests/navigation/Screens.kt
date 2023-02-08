package com.kuluruvineeth.pointsofinterests.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kuluruvineeth.pointsofinterests.R

sealed class Screen(
    val route: String,
    @StringRes val name: Int,
    @DrawableRes val icon: Int? = null,
    val isFullScreen: Boolean = false,
    val menuItems: List<MenuItem> = emptyList(),
    val routePath: String = ""
){
    object Home: Screen(
        route = "screen_home",
        R.string.screen_home,
        R.drawable.ic_home,
        menuItems = arrayListOf(MenuItem.Search,MenuItem.SortBy)
    )
    object Categories : Screen(
        route = "screen_categories",
        R.string.screen_categories,
        isFullScreen = true
    )

    object CategoriesList : Screen(
        route = "screen_categories_list",
        R.string.screen_categories,
        R.drawable.ic_category,
        isFullScreen = true,
        menuItems = arrayListOf(MenuItem.Add)
    )

    object CategoriesDetailed : Screen(
        route = "screen_category_detailed?categoryId={categoryId}",
        routePath = "screen_category_detailed",
        name = R.string.screen_categories,
        isFullScreen = true,
    ) {
        const val ARG_CATEGORY_ID = "categoryId"
    }

    object Profile : Screen(
        route = "screen_profile",
        R.string.screen_profile,
        R.drawable.ic_profile
    )

    object About : Screen(
        route = "screen_about",
        R.string.screen_about,
        isFullScreen = true
    )

    object CreatePoi : Screen(
        route = "screen_create_poi",
        R.string.screen_create_poi, isFullScreen = true
    )

    companion object{
        val all = arrayListOf(
            Home,Categories,Profile,CreatePoi,CategoriesList,CategoriesDetailed,About
        )
    }
}

fun getMainScreens() = arrayListOf(Screen.Home,Screen.Profile)

fun routeToScreen(route: String?) : Screen? = Screen.all.find { it.route == route }
