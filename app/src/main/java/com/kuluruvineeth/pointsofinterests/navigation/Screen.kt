package com.kuluruvineeth.pointsofinterests.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kuluruvineeth.pointsofinterests.R

sealed class Screen(
    val route: String,
    @StringRes val name: Int,
    @DrawableRes val icon: Int? = null,
    val isFullScreen: Boolean = false
){
    object Home: Screen(
        route = "screen_home",
        R.string.screen_home,
        R.drawable.ic_home
    )
    object Categories : Screen(
        route = "screen_categories",
        R.string.screen_categories,
        R.drawable.ic_category
    )
    object Profile : Screen(
        route = "screen_profile",
        R.string.screen_profile,
        R.drawable.ic_profile
    )

    object CreatePoi : Screen(
        route = "screen_create_poi",
        R.string.screen_create_poi, isFullScreen = true
    )

    companion object{
        val all = arrayListOf(
            Home,Categories,Profile,CreatePoi
        )
    }
}

fun getMainScreens() = arrayListOf(Screen.Home,Screen.Categories,Screen.Profile)

fun routeToScreen(route: String?) : Screen? = Screen.all.find { it.route == route }
