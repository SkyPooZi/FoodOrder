package com.example.foodorder.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.example.foodorder.ui.screen.component.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodorder.data.local.entity.OrderWithItems
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrdersScreen(
    orders: List<OrderWithItems>,
    cartCount: Int,
    onMenuClick: () -> Unit,
    onCartClick: () -> Unit,
    onStartShopping: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID")) }

    Scaffold(
        topBar = {
            FoodOrderTopBar("Pesanan", cartCount, onMenuClick, onCartClick)
        }
    ) { padding ->
        if (orders.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.ShoppingBag,
                title = "Belum ada pesanan",
                subtitle = "Pesanan yang kamu buat akan muncul di sini.",
                modifier = Modifier.padding(padding),
                actionLabel = "Pesan Sekarang",
                onAction = onStartShopping
            )
        } else {
            AdaptiveContainer(modifier = Modifier.padding(padding)) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders, key = { it.order.id }) { data ->
                        val order = data.order
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.large,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text("Pesanan #${order.id}", style = MaterialTheme.typography.titleMedium)
                                        Text(
                                            dateFormat.format(Date(order.createdAt)),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Surface(
                                        shape = MaterialTheme.shapes.small,
                                        color = MaterialTheme.colorScheme.secondaryContainer
                                    ) {
                                        Text(
                                            "Diproses",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                                HorizontalDivider(Modifier.padding(vertical = 10.dp))
                                data.items.forEach { item ->
                                    PriceRow(
                                        label = "${item.quantity}x ${item.foodName}",
                                        value = formatRupiah(item.price * item.quantity)
                                    )
                                }
                                HorizontalDivider(Modifier.padding(vertical = 10.dp))
                                PriceRow("Ongkir", formatRupiah(order.shipping))
                                PriceRow("Total", formatRupiah(order.total), emphasized = true)
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "Dikirim ke: ${order.address}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "Pembayaran: ${order.paymentMethod}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
