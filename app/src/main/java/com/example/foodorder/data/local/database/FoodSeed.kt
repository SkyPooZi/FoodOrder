package com.example.foodorder.data.local.database

import com.example.foodorder.data.local.entity.FoodEntity

object FoodSeed {
    val foods = listOf(
        FoodEntity(
            name = "Nasi Goreng Spesial",
            description = "Nasi goreng dengan bumbu spesial, telur, dan sayuran segar.",
            price = 18000, category = "Makanan", emoji = "🍛", isPromo = true
        ),
        FoodEntity(
            name = "Ayam Geprek",
            description = "Ayam crispy yang digeprek dengan sambal bawang level pedas pilihanmu.",
            price = 20000, category = "Makanan", emoji = "🍗"
        ),
        FoodEntity(
            name = "Mie Ayam Bakso",
            description = "Mie kenyal dengan topping ayam kecap, bakso sapi, dan pangsit goreng.",
            price = 15000, category = "Makanan", emoji = "🍜"
        ),
        FoodEntity(
            name = "Sate Ayam",
            description = "10 tusuk sate ayam bumbu kacang disajikan dengan lontong.",
            price = 25000, category = "Makanan", emoji = "🍢"
        ),
        FoodEntity(
            name = "Soto Ayam",
            description = "Soto kuah kuning hangat dengan suwiran ayam, telur, dan koya.",
            price = 17000, category = "Makanan", emoji = "🍲"
        ),
        FoodEntity(
            name = "Gado-Gado",
            description = "Sayuran rebus, tahu, tempe, dan lontong dengan saus kacang.",
            price = 16000, category = "Makanan", emoji = "🥗"
        ),
        FoodEntity(
            name = "Es Teh Manis",
            description = "Teh seduh dingin dengan gula asli, segar diminum kapan saja.",
            price = 5000, category = "Minuman", emoji = "🧋", isPromo = true
        ),
        FoodEntity(
            name = "Es Jeruk",
            description = "Perasan jeruk peras asli dengan es batu.",
            price = 8000, category = "Minuman", emoji = "🍊"
        ),
        FoodEntity(
            name = "Kopi Susu",
            description = "Espresso dengan susu segar dan gula aren.",
            price = 15000, category = "Minuman", emoji = "☕"
        ),
        FoodEntity(
            name = "Jus Alpukat",
            description = "Alpukat mentega dengan susu cokelat, kental dan creamy.",
            price = 14000, category = "Minuman", emoji = "🥑"
        ),
        FoodEntity(
            name = "Kentang Goreng",
            description = "Kentang goreng renyah dengan taburan bumbu balado.",
            price = 12000, category = "Camilan", emoji = "🍟"
        ),
        FoodEntity(
            name = "Pisang Goreng",
            description = "Pisang kepok goreng tepung dengan topping keju dan cokelat.",
            price = 10000, category = "Camilan", emoji = "🍌", isPromo = true
        ),
        FoodEntity(
            name = "Tahu Crispy",
            description = "Tahu goreng tepung renyah dengan cabai garam.",
            price = 9000, category = "Camilan", emoji = "🥟"
        )
    )
}
