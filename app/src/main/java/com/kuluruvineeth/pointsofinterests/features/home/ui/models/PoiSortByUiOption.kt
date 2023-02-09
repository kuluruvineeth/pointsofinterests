package com.kuluruvineeth.pointsofinterests.features.home.ui.models


import com.kuluruvineeth.pointsofinterests.R

enum class PoiSortByUiOption{
    SEVERITY, DATE, TITLE, NONE
}

fun PoiSortByUiOption.toTitle() = when(this){
    PoiSortByUiOption.SEVERITY -> R.string.title_sort_by_severity
    PoiSortByUiOption.DATE -> R.string.title_sort_by_date
    PoiSortByUiOption.TITLE -> R.string.title_sort_by_title
    else -> R.string.empty
}

fun PoiSortByUiOption.toDomain() = when (this){

    PoiSortByUiOption.SEVERITY -> PoiSortByUiOption.SEVERITY
    PoiSortByUiOption.TITLE -> PoiSortByUiOption.TITLE
    else -> PoiSortByUiOption.DATE
}