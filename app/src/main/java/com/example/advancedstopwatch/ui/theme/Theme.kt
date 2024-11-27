package com.example.advancedstopwatch.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Définir vos couleurs manuellement ou importer les couleurs depuis un fichier colors.xml
val md_theme_light_primary = Color(0xFF6200EE)  // Exemple de couleur
val md_theme_light_onPrimary = Color(0xFFFFFFFF) // Exemple de couleur
val md_theme_light_primaryContainer = Color(0xFFBB86FC)
val md_theme_light_onPrimaryContainer = Color(0xFF3700B3)
val md_theme_light_secondary = Color(0xFF03DAC6)
val md_theme_light_onSecondary = Color(0xFF000000)
val md_theme_light_background = Color(0xFF121212)
val md_theme_light_onBackground = Color(0xFFFFFFFF)

// Ajoutez les autres couleurs ici

private val LightColors = lightColorScheme(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    secondary = md_theme_light_secondary,
    onSecondary = md_theme_light_onSecondary,
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    // Ajoutez ici les autres couleurs nécessaires pour votre thème
)

val md_theme_dark_primary = Color(0xFF3700B3)  // Exemple pour le thème sombre
val md_theme_dark_onPrimary = Color(0xFFFFFFFF)
val md_theme_dark_primaryContainer = Color(0xFFBB86FC)
val md_theme_dark_onPrimaryContainer = Color(0xFF3700B3)
val md_theme_dark_secondary = Color(0xFF03DAC6)
val md_theme_dark_onSecondary = Color(0xFF000000)
val md_theme_dark_background = Color(0xFF121212)
val md_theme_dark_onBackground = Color(0xFFFFFFFF)

// Définir ici les couleurs pour le thème sombre

private val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    // Ajoutez ici les autres couleurs nécessaires pour votre thème sombre
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
