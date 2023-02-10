package com.kuluruvineeth.pointsofinterests.core.utils

import com.kuluruvineeth.pointsofinterests.R

sealed class ErrorDisplayObject(val errorMessage: Int){
    object GenericError : ErrorDisplayObject(R.string.title_ui_state_error)
}

fun Throwable.toDisplayObject(): ErrorDisplayObject{
    return ErrorDisplayObject.GenericError
}