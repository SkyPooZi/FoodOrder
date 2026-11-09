package com.example.foodorder.ui.screen.component

import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Int): String {
    val number = NumberFormat.getInstance(Locale.forLanguageTag("id-ID")).format(amount)
    return "Rp$number"
}
