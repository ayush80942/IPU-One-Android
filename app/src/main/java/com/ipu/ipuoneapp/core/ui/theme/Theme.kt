package com.ipu.ipuoneapp.core.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,

    secondary = Secondary,
    onSecondary = Color.Black,

    background = Background,
    onBackground = TextPrimary,

    surface = Color.White,
    onSurface = TextPrimary,

    error = Color.Red
)

@Composable
fun IPUOneAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}