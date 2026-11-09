package com.example.foodorder.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorder.data.local.entity.FoodEntity
import com.example.foodorder.ui.screen.component.BackTopBar
import com.example.foodorder.ui.screen.component.FoodImage
import com.example.foodorder.ui.screen.component.QuantitySelector
import com.example.foodorder.ui.screen.component.SuccessDialog
import com.example.foodorder.ui.screen.component.formatRupiah
import com.example.foodorder.ui.screen.component.isWideScreen
import com.example.foodorder.ui.theme.priceColor
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    food: FoodEntity?,
    addedFoodName: String?,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAddToCart: (quantity: Int) -> Unit,
    onDismissDialog: () -> Unit,
    onViewCart: () -> Unit,
    onContinueShopping: () -> Unit
) {
    var quantity by rememberSaveable { mutableIntStateOf(1) }
    val wide = isWideScreen()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            BackTopBar(
                title = "",
                onBack = onBack,
                actions = {
                    if (food != null) {
                        IconButton(
                            onClick = {
                                onToggleFavorite()
                                scope.launch {
                                    snackbarHostState.currentSnackbarData?.dismiss()
                                    snackbarHostState.showSnackbar(
                                        message = if (food.isFavorite) "Dihapus dari favorit" else "Ditambahkan ke favorit",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (food.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favorit",
                                tint = if (food.isFavorite) MaterialTheme.colorScheme.error else LocalContentColor.current
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (food == null) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(Modifier.padding(padding).fillMaxSize()) {
                Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                    if (wide) {
                        Row(
                            modifier = Modifier.padding(24.dp),
                            horizontalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            FoodImage(
                                emoji = food.emoji,
                                emojiSize = 120.sp,
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(MaterialTheme.shapes.large)
                            )
                            FoodInfo(
                                food = food,
                                quantity = quantity,
                                onQuantityChange = { quantity = it },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        FoodImage(
                            emoji = food.emoji,
                            emojiSize = 96.sp,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(MaterialTheme.shapes.large)
                        )
                        Spacer(Modifier.height(16.dp))
                        FoodInfo(
                            food = food,
                            quantity = quantity,
                            onQuantityChange = { quantity = it },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }

                Box(Modifier.fillMaxWidth().padding(16.dp)) {
                    Button(
                        onClick = { onAddToCart(quantity) },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Add to Cart")
                    }
                }
            }
        }
    }

    if (addedFoodName != null) {
        SuccessDialog(
            title = "Berhasil Ditambahkan!",
            message = "$addedFoodName telah ditambahkan ke keranjang.",
            primaryLabel = "Lihat Keranjang",
            onPrimary = onViewCart,
            onDismiss = onDismissDialog,
            secondaryLabel = "Lanjut Belanja",
            onSecondary = onContinueShopping
        )
    }
}

@Composable
private fun FoodInfo(
    food: FoodEntity,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(food.name, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(4.dp))
        Text(
            formatRupiah(food.price),
            style = MaterialTheme.typography.titleLarge,
            color = priceColor(),
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        Text(
            food.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            QuantitySelector(quantity = quantity, onQuantityChange = onQuantityChange)
        }
        Spacer(Modifier.height(16.dp))
    }
}
