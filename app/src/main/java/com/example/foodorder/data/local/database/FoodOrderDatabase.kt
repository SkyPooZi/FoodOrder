package com.example.foodorder.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodorder.data.local.dao.CartDao
import com.example.foodorder.data.local.dao.FoodDao
import com.example.foodorder.data.local.dao.OrderDao
import com.example.foodorder.data.local.entity.CartItemEntity
import com.example.foodorder.data.local.entity.FoodEntity
import com.example.foodorder.data.local.entity.OrderEntity
import com.example.foodorder.data.local.entity.OrderItemEntity

@Database(
    entities = [
        FoodEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FoodOrderDatabase : RoomDatabase() {

    abstract fun foodDao(): FoodDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: FoodOrderDatabase? = null

        fun getInstance(context: Context): FoodOrderDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FoodOrderDatabase::class.java,
                    "foodorder.db"
                ).build().also { INSTANCE = it }
            }
    }
}
