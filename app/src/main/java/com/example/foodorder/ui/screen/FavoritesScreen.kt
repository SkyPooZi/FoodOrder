package com.example.foodorder.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.example.foodorder.ui.screen.component.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorder.data.local.entity.FoodEntity

@Composable
fun FavoritesScreen(
    favorites: List<FoodEntity>,
    cartCount: Int,
    onMenuClick: () -> Unit,
    onCartClick: () -> Unit,
    onFoodClick: (Int) -> Unit,
    onToggleFavorite: (FoodEntity) -> Unit,
    onStartShopping: () -> Unit
) {
    Scaffold(
        topBar = { FoodOrderTopBar("Favorit", cartCount, onMenuClick, onCartClick) }
    ) { padding ->
        if (favorites.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.Favorite,
                title = "Belum ada favorit",
                subtitle = "Tekan ikon hati di halaman detail untuk menyimpan makanan favoritmu.",
                modifier = Modifier.padding(padding),
                actionLabel = "Cari Makanan",
                onAction = onStartShopping
            )
        } else {
            AdaptiveContainer(modifier = Modifier.padding(padding)) {
                LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
                    items(favorites, key = { it.id }) { food ->
                        ListItem(
                            modifier = Modifier.clickable { onFoodClick(food.id) },
                            headlineContent = { Text(food.name, style = MaterialTheme.typography.titleSmall) },
                            supportingContent = { Text(formatRupiah(food.price)) },
                            leadingContent = {
                                FoodImage(
                                    emoji = food.emoji,
                                    emojiSize = 28.sp,
                                    modifier = Modifier.size(56.dp).clip(MaterialTheme.shapes.medium)
                                )
                            },
                            trailingContent = {
                                IconButton(onClick = { onToggleFavorite(food) }) {
                                    Icon(
                                        Icons.Filled.Favorite,
                                        contentDescription = "Hapus dari favorit",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
