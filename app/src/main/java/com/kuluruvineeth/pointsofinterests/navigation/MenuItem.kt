package com.kuluruvineeth.pointsofinterests.navigation

import androidx.annotation.DrawableRes
import com.kuluruvineeth.pointsofinterests.R


sealed class MenuItem(
    @DrawableRes val icon: Int,
    val action: MenuActionType
){
    object Back : MenuItem(R.drawable.ic_back,MenuActionType.BACK)
    object Search : MenuItem(R.drawable.ic_search, MenuActionType.SEARCH)
}

enum class MenuActionType{
    BACK,
    SEARCH
}