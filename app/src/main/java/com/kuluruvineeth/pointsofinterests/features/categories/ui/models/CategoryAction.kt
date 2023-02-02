package com.kuluruvineeth.pointsofinterests.features.categories.ui.models

import androidx.annotation.StringRes
import com.kuluruvineeth.pointsofinterests.R

enum class CategoryAction(@StringRes val title: Int) {

    DELETE(R.string.delete),
    EDIT(R.string.edit)
}