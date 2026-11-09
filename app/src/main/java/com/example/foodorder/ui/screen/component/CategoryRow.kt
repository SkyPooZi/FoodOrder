package com.example.foodorder.ui.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorder.viewmodel.FoodCategory

private data class CategoryStyle(val emoji: String, val color: Color)

private fun styleOf(category: FoodCategory): CategoryStyle = when (category) {
    FoodCategory.MAKANAN -> CategoryStyle("🍛", Color(0xFFFF9800))
    FoodCategory.MINUMAN -> CategoryStyle("🥤", Color(0xFF42A5F5))
    FoodCategory.CAMILAN -> CategoryStyle("🍟", Color(0xFFFFCA28))
    FoodCategory.PROMO -> CategoryStyle("🎁", Color(0xFFEC407A))
    FoodCategory.ALL -> CategoryStyle("🍽️", Color(0xFF7646DB))
}

@Composable
fun CategoryRow(
    selected: FoodCategory,
    onSelected: (FoodCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FoodCategory.entries.filter { it != FoodCategory.ALL }.forEach { category ->
            val isSelected = category == selected
            CategoryTile(
                category = category,
                selected = isSelected,
                onClick = { onSelected(if (isSelected) FoodCategory.ALL else category) }
            )
        }
    }
}

@Composable
private fun CategoryTile(
    category: FoodCategory,
    selected: Boolean,
    onClick: () -> Unit
) {
    val style = styleOf(category)
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(shape)
                .background(style.color.copy(alpha = if (selected) 0.40f else 0.20f))
                .then(
                    if (selected) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, shape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(style.emoji, fontSize = 26.sp)
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = category.label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
