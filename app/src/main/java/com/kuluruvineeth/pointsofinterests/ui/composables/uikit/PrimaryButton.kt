package com.kuluruvineeth.pointsofinterests.ui.composables.uikit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuluruvineeth.pointsofinterests.ui.theme.OrangeMain
import com.kuluruvineeth.pointsofinterests.ui.theme.OrangeSuperLight
import com.kuluruvineeth.pointsofinterests.ui.theme.White


@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    paddingsVertical: Dp = 16.dp,
    paddingsHorizontal: Dp = 48.dp
) {
    ElevatedButton(
        contentPadding = PaddingValues(
            horizontal = paddingsHorizontal,
            vertical = paddingsVertical
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = OrangeMain,
            contentColor = White,
            disabledContainerColor = OrangeSuperLight,
            disabledContentColor = White
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text.uppercase(), color = White)
    }
}