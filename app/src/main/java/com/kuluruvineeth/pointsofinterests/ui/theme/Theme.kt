package com.kuluruvineeth.pointsofinterests.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = OrangeMain,
    onPrimary = White,
    secondary = OrangeDark,
    onSecondary = White,
    tertiary = OrangeSuperLight,
    onTertiary = DarkMainColor,
    background = Dark,
    onBackground = White,
    surface = Dark,
    onSurface = White,
    error = ErrorColor,
    onError = White
)

private val LightColorScheme = lightColorScheme(
    primary = OrangeMain,
    onPrimary = White,
    secondary = OrangeDark,
    onSecondary = White,
    tertiary = OrangeSuperLight,
    onTertiary = DarkMainColor,
    background = White,
    onBackground = DarkMainColor,
    surface = White,
    onSurface = DarkMainColor,
    error = ErrorColor,
    onError = White
)

@Composable
fun PointsOfInterestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when{
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if(darkTheme)
                dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if(!view.isInEditMode){
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme.not()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}