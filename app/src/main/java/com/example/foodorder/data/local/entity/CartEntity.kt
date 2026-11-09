package com.example.foodorder.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = FoodEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CartItemEntity(
    @PrimaryKey val foodId: Int,
    val quantity: Int
)

data class CartItemDetail(
    val foodId: Int,
    val name: String,
    val price: Int,
    val emoji: String,
    val quantity: Int
)
