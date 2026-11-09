package com.example.foodorder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val price: Int,
    val category: String,
    val emoji: String,
    val isFavorite: Boolean = false,
    val isPromo: Boolean = false
)
