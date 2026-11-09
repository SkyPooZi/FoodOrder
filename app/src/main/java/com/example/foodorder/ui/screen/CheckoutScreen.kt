package com.example.foodorder.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.foodorder.ui.screen.component.AdaptiveContainer
import com.example.foodorder.ui.screen.component.BackTopBar
import com.example.foodorder.ui.screen.component.PriceRow
import com.example.foodorder.ui.screen.component.SuccessDialog
import com.example.foodorder.ui.screen.component.formatRupiah
import com.example.foodorder.viewmodel.CartUiState

private val paymentMethods = listOf(
    "Transfer Bank",
    "E-Wallet (OVO, GoPay, DANA)",
    "QRIS"
)

@Composable
fun CheckoutScreen(
    cart: CartUiState,
    onBack: () -> Unit,
    onPlaceOrder: (address: String, payment: String, onSuccess: () -> Unit) -> Unit,
    onOrderSuccess: () -> Unit
) {
    var address by rememberSaveable { mutableStateOf("") }
    var addressError by rememberSaveable { mutableStateOf(false) }
    var payment by rememberSaveable { mutableStateOf(paymentMethods.first()) }
    var showSuccess by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = { BackTopBar(title = "Checkout", onBack = onBack) }) { padding ->
        AdaptiveContainer(modifier = Modifier.padding(padding)) {
            Column(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionTitle("Alamat Pengiriman")
                    OutlinedTextField(
                        value = address,
                        onValueChange = {
                            address = it
                            addressError = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Jl. Pendidikan No. 10, Sidoarjo") },
                        leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                        isError = addressError,
                        supportingText = if (addressError) {
                            { Text("Alamat tidak boleh kosong") }
                        } else null,
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        minLines = 2
                    )

                    Spacer(Modifier.height(8.dp))
                    SectionTitle("Metode Pembayaran")
                    Column(Modifier.selectableGroup()) {
                        paymentMethods.forEach { method ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = method == payment,
                                        onClick = { payment = method },
                                        role = Role.RadioButton
                                    )
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = method == payment, onClick = null)
                                Spacer(Modifier.width(12.dp))
                                Text(method, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    SectionTitle("Ringkasan Pesanan")
                    cart.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                item.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "x${item.quantity}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            Text(
                                formatRupiah(item.price * item.quantity),
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.End,
                                modifier = Modifier.widthIn(min = 80.dp)
                            )
                        }
                    }
                    HorizontalDivider(Modifier.padding(vertical = 4.dp))
                    PriceRow("Subtotal", formatRupiah(cart.subtotal))
                    PriceRow("Ongkir", formatRupiah(cart.shipping))
                }

                Surface(
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    tonalElevation = 2.dp,
                    shadowElevation = 8.dp
                ) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        PriceRow("Total Pembayaran", formatRupiah(cart.total), emphasized = true)
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = {
                                if (address.isBlank()) {
                                    addressError = true
                                } else {
                                    onPlaceOrder(address.trim(), payment) { showSuccess = true }
                                }
                            },
                            enabled = cart.items.isNotEmpty() && !showSuccess,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth().height(52.dp)
                        ) { Text("Buat Pesanan") }
                    }
                }
            }
        }
    }

    if (showSuccess) {
        SuccessDialog(
            title = "Pesanan Berhasil!",
            message = "Pesananmu sedang diproses. Kamu bisa melihatnya di halaman Pesanan.",
            primaryLabel = "Lihat Pesanan",
            onPrimary = onOrderSuccess,
            onDismiss = onOrderSuccess
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleSmall)
}
