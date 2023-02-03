package com.kuluruvineeth.pointsofinterests.core.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.kuluruvineeth.pointsofinterests.features.categories.ui.models.CategoryUiModel

/*
@Composable
@ReadOnlyComposable
fun stringFromResource(
    @StringRes res: Int
) : String? = stringFromResource(res = res)

 */

fun List<CategoryUiModel>.containsId(id: String) = this.any { it.id == id }