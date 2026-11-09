package com.example.foodorder.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.foodorder.data.local.entity.FoodEntity
import com.example.foodorder.ui.screen.component.AdaptiveContainer
import com.example.foodorder.ui.screen.component.CategoryRow
import com.example.foodorder.ui.screen.component.FoodCard
import com.example.foodorder.ui.screen.component.FoodOrderTopBar
import com.example.foodorder.ui.screen.component.GreetingCard
import com.example.foodorder.viewmodel.FoodCategory

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun HomeScreen(
    foods: List<FoodEntity>,
    query: String,
    selectedCategory: FoodCategory,
    cartCount: Int,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (FoodCategory) -> Unit,
    onFoodClick: (Int) -> Unit,
    onMenuClick: () -> Unit,
    onCartClick: () -> Unit
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val columns = (screenWidthDp / 180).coerceIn(2, 5)

    Scaffold(
        topBar = {
            FoodOrderTopBar(
                title = "FoodOrder",
                cartCount = cartCount,
                onMenuClick = onMenuClick,
                onCartClick = onCartClick
            )
        }
    ) { padding ->
        AdaptiveContainer(modifier = Modifier.padding(padding), maxWidth = 1000.dp) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { GreetingCard() }

                item {
                    OutlinedTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Cari makanan...") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        trailingIcon = if (query.isNotEmpty()) {
                            {
                                IconButton(onClick = { onQueryChange("") }) {
                                    Icon(Icons.Filled.Clear, contentDescription = "Hapus pencarian")
                                }
                            }
                        } else null,
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }

                item {
                    CategoryRow(selected = selectedCategory, onSelected = onCategorySelected)
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedCategory == FoodCategory.ALL) "Rekomendasi" else selectedCategory.label,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = {
                                onCategorySelected(FoodCategory.ALL)
                                onQueryChange("")
                            }
                        ) {
                            Text("Lihat Semua")
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                if (foods.isEmpty()) {
                    item {
                        Text(
                            text = "Makanan tidak ditemukan.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 24.dp)
                        )
                    }
                } else {
                    items(foods.chunked(columns), key = { row -> row.first().id }) { rowFoods ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            rowFoods.forEach { food ->
                                FoodCard(
                                    food = food,
                                    onClick = { onFoodClick(food.id) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            repeat(columns - rowFoods.size) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
