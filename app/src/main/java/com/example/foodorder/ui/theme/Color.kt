package com.example.foodorder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

val PurplePrimary = Color(0xFF7646DB)
val PurpleOnPrimary = Color(0xFFFFFFFF)
val PurplePrimaryContainer = Color(0xFFEADFFF)
val PurpleOnPrimaryContainer = Color(0xFF25005F)
val PurpleSecondary = Color(0xFF625B71)
val PurpleSecondaryContainer = Color(0xFFE8DEF8)
val PurpleOnSecondaryContainer = Color(0xFF1E192B)
val PinkTertiary = Color(0xFFB4457A)
val PinkTertiaryContainer = Color(0xFFFFD8E8)
val PinkOnTertiaryContainer = Color(0xFF3E001F)
val LightBackground = Color(0xFFFBF8FF)
val LightOnBackground = Color(0xFF1D1B20)
val LightSurfaceVariant = Color(0xFFE7E0EB)
val LightOnSurfaceVariant = Color(0xFF49454E)
val LightOutline = Color(0xFF7A757F)
val LightOutlineVariant = Color(0xFFE3DCEE)
val ErrorLight = Color(0xFFBA1A1A)

val PurplePrimaryDark = Color(0xFFCFBCFF)
val PurpleOnPrimaryDark = Color(0xFF381E72)
val PurplePrimaryContainerDark = Color(0xFF4F2FA8)
val PurpleOnPrimaryContainerDark = Color(0xFFE9DDFF)
val PurpleSecondaryDark = Color(0xFFCCC2DC)
val PurpleSecondaryContainerDark = Color(0xFF4A4458)
val PurpleOnSecondaryContainerDark = Color(0xFFE8DEF8)
val PinkTertiaryDark = Color(0xFFFFB0D2)
val PinkTertiaryContainerDark = Color(0xFF7B2950)
val PinkOnTertiaryContainerDark = Color(0xFFFFD8E8)
val DarkBackground = Color(0xFF141218)
val DarkOnBackground = Color(0xFFE6E0E9)
val DarkSurfaceVariant = Color(0xFF49454E)
val DarkOnSurfaceVariant = Color(0xFFCAC4CF)
val DarkOutline = Color(0xFF948F99)
val DarkOutlineVariant = Color(0xFF49454E)
val ErrorDark = Color(0xFFFFB4AB)

val SuccessGreen = Color(0xFF2E9E5B)

val PriceRedLight = Color(0xFFD32F2F)
val PriceRedDark = Color(0xFFFF8A80)

@Composable
@ReadOnlyComposable
fun priceColor(): Color =
    if (MaterialTheme.colorScheme.background.luminance() < 0.5f) PriceRedDark else PriceRedLight
