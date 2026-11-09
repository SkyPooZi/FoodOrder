package com.example.foodorder.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorder.data.local.entity.CartItemDetail
import com.example.foodorder.ui.screen.component.AdaptiveContainer
import com.example.foodorder.ui.screen.component.BackTopBar
import com.example.foodorder.ui.screen.component.EmptyState
import com.example.foodorder.ui.screen.component.FoodImage
import com.example.foodorder.ui.screen.component.PriceRow
import com.example.foodorder.ui.screen.component.QuantitySelector
import com.example.foodorder.ui.screen.component.formatRupiah
import com.example.foodorder.ui.theme.priceColor
import com.example.foodorder.viewmodel.CartUiState
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    cart: CartUiState,
    onBack: () -> Unit,
    onQuantityChange: (foodId: Int, quantity: Int) -> Unit,
    onRemove: (foodId: Int) -> Unit,
    onUndoRemove: (foodId: Int, quantity: Int) -> Unit,
    onCheckout: () -> Unit,
    onStartShopping: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { BackTopBar(title = "Keranjang", onBack = onBack) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (cart.items.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.ShoppingCart,
                title = "Keranjang masih kosong",
                subtitle = "Yuk pilih makanan favoritmu dulu.",
                modifier = Modifier.padding(padding),
                actionLabel = "Mulai Belanja",
                onAction = onStartShopping
            )
        } else {
            AdaptiveContainer(modifier = Modifier.padding(padding)) {
                Column(Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(cart.items, key = { it.foodId }) { item ->
                            CartItemCard(
                                item = item,
                                onQuantityChange = { onQuantityChange(item.foodId, it) },
                                onRemove = {
                                    onRemove(item.foodId)
                                    scope.launch {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        val result = snackbarHostState.showSnackbar(
                                            message = "${item.name} dihapus dari keranjang",
                                            actionLabel = "Batalkan",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result == SnackbarResult.ActionPerformed) {
                                            onUndoRemove(item.foodId, item.quantity)
                                        }
                                    }
                                }
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        tonalElevation = 2.dp,
                        shadowElevation = 8.dp
                    ) {
                        Column(Modifier.fillMaxWidth().padding(16.dp)) {
                            PriceRow("Subtotal", formatRupiah(cart.subtotal))
                            PriceRow("Ongkir", formatRupiah(cart.shipping))
                            HorizontalDivider(Modifier.padding(vertical = 8.dp))
                            PriceRow("Total", formatRupiah(cart.total), emphasized = true)
                            Spacer(Modifier.height(12.dp))
                            Button(
                                onClick = onCheckout,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.fillMaxWidth().height(52.dp)
                            ) { Text("Checkout") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(
    item: CartItemDetail,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FoodImage(
                emoji = item.emoji,
                emojiSize = 32.sp,
                modifier = Modifier.size(72.dp).clip(MaterialTheme.shapes.medium)
            )
            Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(
                    item.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    formatRupiah(item.price),
                    style = MaterialTheme.typography.bodyMedium,
                    color = priceColor(),
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                QuantitySelector(
                    quantity = item.quantity,
                    onQuantityChange = onQuantityChange,
                    compact = true
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Hapus ${item.name}",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
