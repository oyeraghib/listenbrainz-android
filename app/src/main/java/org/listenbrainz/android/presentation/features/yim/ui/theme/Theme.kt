package org.listenbrainz.android.presentation.features.yim.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val RedColorScheme = darkColorScheme(
    background = yimRed,
    onBackground = yimYellow,
    onSurface = yimYellow,
    surface = yimWhite
    
)

private val YellowColorScheme = lightColorScheme(
    background = yimYellow,
    onBackground = yimRed,
    onSurface = yimRed,
    surface = yimWhite,
    
)

@Immutable
data class YimPaddings(
    val DefaultPadding: Dp = 16.dp,
    val tinyPadding: Dp = 4.dp,
    val smallPadding: Dp = 8.dp,
    val largePadding: Dp = 24.dp,
    val extraLargePadding: Dp = 32.dp
)
internal val LocalYimPaddings = staticCompositionLocalOf { YimPaddings() }


@Composable
fun YearInMusicTheme(
    redTheme: Boolean,
    systemUiController : SystemUiController = rememberSystemUiController(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (redTheme){
        true -> RedColorScheme
        else -> YellowColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.background.toArgb()
            val isDark = when (redTheme){
                true -> false
                else -> true
            }
            systemUiController.statusBarDarkContentEnabled = isDark
            systemUiController.navigationBarDarkContentEnabled = isDark
            systemUiController.setNavigationBarColor(color = colorScheme.background)
        }
    }
    CompositionLocalProvider {
        LocalYimPaddings provides YimPaddings()
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}