package com.kuluruvineeth.pointsofinterests.ui.composables.uistates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kuluruvineeth.pointsofinterests.R
import com.kuluruvineeth.pointsofinterests.ui.composables.uikit.PrimaryButton
import com.kuluruvineeth.pointsofinterests.ui.theme.DarkMainColor
import com.kuluruvineeth.pointsofinterests.ui.theme.WarmGray400

@Composable
fun ErrorView(
    background: Color = MaterialTheme.colorScheme.background,
    title: String = stringResource(id = R.string.title_ui_state_error),
    message: String? = null,
    icon: Int = R.drawable.ic_error,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    onRetryClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(150.dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.7f
                    )
                )
        ){
            Icon(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = "Error icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = textColor,
            maxLines = 1
        )

        if(message != null){

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }

        if(onRetryClick != null){

            Spacer(modifier = Modifier.height(64.dp))

            PrimaryButton(
                text = stringResource(id = R.string.button_title_try_again),
                onClick = onRetryClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorStatePreview() {
    ErrorView(
        message = "Network connection issue",
        onRetryClick = {}
    )
}