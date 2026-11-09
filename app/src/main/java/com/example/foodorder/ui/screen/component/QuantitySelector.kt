package com.example.foodorder.ui.screen.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    minQuantity: Int = 1,
    maxQuantity: Int = 99
) {
    val buttonSize = if (compact) 30.dp else 40.dp
    val iconSize = if (compact) 16.dp else 20.dp
    val primary = MaterialTheme.colorScheme.primary
    val outline = MaterialTheme.colorScheme.outlineVariant

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        OutlinedIconButton(
            onClick = { onQuantityChange(quantity - 1) },
            enabled = quantity > minQuantity,
            modifier = Modifier.size(buttonSize),
            colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = primary),
            border = BorderStroke(1.5.dp, if (quantity > minQuantity) primary else outline)
        ) {
            Icon(Icons.Filled.Remove, contentDescription = "Kurangi jumlah", modifier = Modifier.size(iconSize))
        }
        Text(
            text = quantity.toString(),
            modifier = Modifier.widthIn(min = if (compact) 36.dp else 56.dp),
            textAlign = TextAlign.Center,
            style = if (compact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleLarge
        )
        OutlinedIconButton(
            onClick = { onQuantityChange(quantity + 1) },
            enabled = quantity < maxQuantity,
            modifier = Modifier.size(buttonSize),
            colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = primary),
            border = BorderStroke(1.5.dp, if (quantity < maxQuantity) primary else outline)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Tambah jumlah", modifier = Modifier.size(iconSize))
        }
    }
}
