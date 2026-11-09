package com.example.foodorder.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.foodorder.data.local.entity.OrderEntity
import com.example.foodorder.data.local.entity.OrderItemEntity
import com.example.foodorder.data.local.entity.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
abstract class OrderDao {

    @Insert
    abstract suspend fun insertOrder(order: OrderEntity): Long

    @Insert
    abstract suspend fun insertItems(items: List<OrderItemEntity>)

    @Transaction
    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    abstract fun observeOrders(): Flow<List<OrderWithItems>>
}
