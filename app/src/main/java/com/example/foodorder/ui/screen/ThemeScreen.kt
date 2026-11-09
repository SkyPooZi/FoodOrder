package com.example.foodorder.ui.screen

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.foodorder.data.repository.ThemeMode
import com.example.foodorder.ui.screen.component.AdaptiveContainer
import com.example.foodorder.ui.screen.component.BackTopBar

@Composable
fun ThemeScreen(
    themeMode: ThemeMode,
    dynamicColor: Boolean,
    onThemeModeChange: (ThemeMode) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
    }
    val dynamicSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    Scaffold(topBar = { BackTopBar(title = "Tema Aplikasi", onBack = onBack) }) { padding ->
        AdaptiveContainer(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ThemeOptionCard(
                    icon = Icons.Filled.LightMode,
                    iconTint = Color(0xFFFFB300),
                    title = "Light Mode",
                    subtitle = "(Terang)",
                    selected = !isDark,
                    containerColor = Color.White,
                    contentColor = Color(0xFF1D1B20),
                    onClick = { onThemeModeChange(ThemeMode.LIGHT) }
                )
                ThemeOptionCard(
                    icon = Icons.Filled.DarkMode,
                    iconTint = Color(0xFFFFE082),
                    title = "Dark Mode",
                    subtitle = "(Gelap)",
                    selected = isDark,
                    containerColor = Color(0xFF1F2236),
                    contentColor = Color.White,
                    onClick = { onThemeModeChange(ThemeMode.DARK) }
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Warna Dinamis", style = MaterialTheme.typography.titleSmall)
                            Text(
                                if (dynamicSupported) "Material You, mengikuti wallpaper perangkat"
                                else "Hanya tersedia di Android 12 ke atas",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = dynamicColor && dynamicSupported,
                            onCheckedChange = onDynamicColorChange,
                            enabled = dynamicSupported
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ColorSwatch(MaterialTheme.colorScheme.primary)
                    ColorSwatch(MaterialTheme.colorScheme.primaryContainer)
                    ColorSwatch(MaterialTheme.colorScheme.tertiaryContainer)
                    ColorSwatch(MaterialTheme.colorScheme.errorContainer)
                }
            }
        }
    }
}

@Composable
private fun ThemeOptionCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    selected: Boolean,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = containerColor, contentColor = contentColor),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(28.dp))
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = contentColor.copy(alpha = 0.7f))
            }
            RadioButton(
                selected = selected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = contentColor.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
private fun ColorSwatch(color: Color) {
    Spacer(
        Modifier
            .size(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(color)
    )
}
