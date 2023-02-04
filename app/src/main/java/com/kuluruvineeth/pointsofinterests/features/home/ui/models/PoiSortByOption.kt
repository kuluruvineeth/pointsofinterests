package com.kuluruvineeth.pointsofinterests.features.home.ui.models


import com.kuluruvineeth.pointsofinterests.R

enum class PoiSortByOption{
    SEVERITY, DATE, TITLE, NONE
}

fun PoiSortByOption.toTitle() = when(this){
    PoiSortByOption.SEVERITY -> R.string.title_sort_by_severity
    PoiSortByOption.DATE -> R.string.title_sort_by_date
    PoiSortByOption.TITLE -> R.string.title_sort_by_title
    else -> R.string.empty
}