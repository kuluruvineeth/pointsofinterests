package com.kuluruvineeth.pointsofinterests.ui.composables.uikit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun OutlineButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    paddingsVertical: Dp = 16.dp,
    paddingsHorizontal: Dp = 48.dp
) {
    val textColor = if (enabled) MaterialTheme.colorScheme.onBackground else
        MaterialTheme.colorScheme.onBackground.copy(0.5f)
    val borderColor = if (enabled) MaterialTheme.colorScheme.primary else
        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)

    OutlinedButton(
        contentPadding = PaddingValues(
            horizontal = paddingsHorizontal,
            vertical = paddingsVertical
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(0.5f),
            disabledContainerColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, borderColor),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text.uppercase(), color = textColor, style = MaterialTheme.typography.titleSmall)
    }
}