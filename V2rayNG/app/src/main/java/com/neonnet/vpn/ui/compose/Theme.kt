package com.neonnet.vpn.ui.compose

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.neonnet.vpn.AppConfig
import com.neonnet.vpn.handler.MmkvManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private val LightColor = lightColorScheme(
    primary = Color(0xFF176B9A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD5ECFA),
    onPrimaryContainer = Color(0xFF082D43),
    secondary = Color(0xFFFF9638),
    onSecondary = Color(0xFF321900),
    secondaryContainer = Color(0xFFFFE2C7),
    onSecondaryContainer = Color(0xFF512900),
    tertiary = Color(0xFFD93645),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDAD9),
    onTertiaryContainer = Color(0xFF410008),
    error = Color(0xFFD93645),
    errorContainer = Color(0xFFFFDAD9),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFF410008),
    background = Color(0xFFF3F6F9),
    onBackground = Color(0xFF18232D),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF18232D),
    surfaceVariant = Color(0xFFE5EDF3),
    onSurfaceVariant = Color(0xFF465563),
    outline = Color(0xFF778895),
    outlineVariant = Color(0xFFD2DDE5),
    inverseSurface = Color(0xFF293744),
    inverseOnSurface = Color(0xFFF3F6F9),
    inversePrimary = Color(0xFFA8DFFF),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFF176B9A),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainer = Color(0xFFEEF3F7),
    surfaceContainerHigh = Color(0xFFE8EEF3),
    surfaceContainerHighest = Color(0xFFDCE5EC),
)

private val DarkColor = darkColorScheme(
    primary = Color(0xFFA8DFFF),
    onPrimary = Color(0xFF00344F),
    primaryContainer = Color(0xFF174D6B),
    onPrimaryContainer = Color(0xFFD5EEFF),
    secondary = Color(0xFFFF9638),
    onSecondary = Color(0xFF321900),
    secondaryContainer = Color(0xFF653600),
    onSecondaryContainer = Color(0xFFFFE2C7),
    tertiary = Color(0xFFF04452),
    onTertiary = Color(0xFF45000A),
    tertiaryContainer = Color(0xFF7C2630),
    onTertiaryContainer = Color(0xFFFFDAD9),
    error = Color(0xFFFF6B75),
    errorContainer = Color(0xFF7C2630),
    onError = Color(0xFF45000A),
    onErrorContainer = Color(0xFFFFDAD9),
    background = Color(0xFF090D12),
    onBackground = Color(0xFFEDF4FA),
    surface = Color(0xFF101720),
    onSurface = Color(0xFFEDF4FA),
    surfaceVariant = Color(0xFF202B36),
    onSurfaceVariant = Color(0xFFB5C2CE),
    outline = Color(0xFF71808D),
    outlineVariant = Color(0xFF293542),
    inverseSurface = Color(0xFFEDF4FA),
    inverseOnSurface = Color(0xFF18232D),
    inversePrimary = Color(0xFF176B9A),
    scrim = Color(0xFF000000),
    surfaceTint = Color(0xFFA8DFFF),
    surfaceContainerLowest = Color(0xFF070B10),
    surfaceContainerLow = Color(0xFF101720),
    surfaceContainer = Color(0xFF151C24),
    surfaceContainerHigh = Color(0xFF1C2631),
    surfaceContainerHighest = Color(0xFF25313D),
)

// Semantic Colors
val colorPing = Color(0xFFA8DFFF)
val colorPingRed = Color(0xFFF04452)
val colorConfigType = Color(0xFFFF9638)
val colorFabActive = Color(0xFFA8DFFF)
val colorFabInactiveLight = Color(0xFF8B949E)
val colorFabInactiveDark = Color(0xFF64717D)
val dividerColorLight = Color(0xFFDCE4EB)
val dividerColorDark = Color(0xFF293542)

// Toast Colors 85%
val toastNormalBgLight = Color(0xD9353A3E) // Dark Gray
val toastNormalBgDark = Color(0xD94A4F54) // Darker Gray
val toastSuccessBg = Color(0xD9388E3C) // Green
val toastErrorBg = Color(0xD9D50000) // Red
val toastInfoBg = Color(0xD93F51B5) // Indigo Blue
val toastIconCircleBg = Color(0x33FFFFFF) // Semi-transparent White
val toastTextColor = Color.White // White

object ThemeManager {
    private val _themeMode = MutableStateFlow(
        MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "2") ?: "2"
    )
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _dynamicColorEnabled = MutableStateFlow(
        MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, false)
    )
    val dynamicColorEnabled: StateFlow<Boolean> = _dynamicColorEnabled.asStateFlow()

    fun setThemeMode(mode: String) {
        MmkvManager.encodeSettings(AppConfig.PREF_UI_MODE_NIGHT, mode)
        _themeMode.value = mode
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        MmkvManager.encodeSettings(AppConfig.PREF_DYNAMIC_COLOR, enabled)
        _dynamicColorEnabled.value = enabled
    }

    fun refresh() {
        _themeMode.value =
            MmkvManager.decodeSettingsString(AppConfig.PREF_UI_MODE_NIGHT, "2") ?: "2"
        _dynamicColorEnabled.value =
            MmkvManager.decodeSettingsBool(AppConfig.PREF_DYNAMIC_COLOR, false)
    }
}

@Composable
fun resolveDarkTheme(): Boolean {
    val mode by ThemeManager.themeMode.collectAsState()
    return when (mode) {
        "1" -> false
        "2" -> true
        else -> isSystemInDarkTheme()
    }
}

val LocalDarkTheme = compositionLocalOf { false }

@Composable
fun AppTheme(
    darkTheme: Boolean = resolveDarkTheme(),
    content: @Composable () -> Unit
) {
    val dynamicColor by ThemeManager.dynamicColorEnabled.collectAsState()
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColor
        else -> LightColor
    }
    val snackbarController = rememberAppSnackbarController()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as? Activity ?: return@SideEffect
            val window = activity.window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme,
        LocalAppSnackbar provides snackbarController
    ) {
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AppSnackbarBridge(controller = snackbarController)
                content()
                AppSnackbarHost(hostState = snackbarController.hostState)
            }
        }
    }
}
