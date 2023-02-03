package com.kuluruvineeth.pointsofinterests.ui.composables.uikit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kuluruvineeth.pointsofinterests.ui.theme.OrangeMain
import com.kuluruvineeth.pointsofinterests.ui.theme.OrangeSuperLight
import com.kuluruvineeth.pointsofinterests.ui.theme.White


@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    paddingsVertical: Dp = 16.dp,
    paddingsHorizontal: Dp = 48.dp
) {
    ElevatedButton(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = paddingsHorizontal,
            vertical = paddingsVertical
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.5f
            ),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(
                alpha = 0.5f
            )
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(text = text.uppercase(),style = MaterialTheme.typography.titleMedium)
    }
}