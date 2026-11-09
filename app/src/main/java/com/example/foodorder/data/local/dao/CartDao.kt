package com.example.foodorder.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.foodorder.data.local.entity.CartItemDetail
import com.example.foodorder.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

private const val CART_DETAIL_QUERY = """
    SELECT f.id AS foodId, f.name AS name, f.price AS price, f.emoji AS emoji, c.quantity AS quantity
    FROM cart_items AS c
    INNER JOIN foods AS f ON f.id = c.foodId
    ORDER BY c.foodId
"""

@Dao
abstract class CartDao {

    @Query(CART_DETAIL_QUERY)
    abstract fun observeCart(): Flow<List<CartItemDetail>>

    @Query(CART_DETAIL_QUERY)
    abstract suspend fun getCartOnce(): List<CartItemDetail>

    @Query("SELECT quantity FROM cart_items WHERE foodId = :foodId")
    abstract suspend fun getQuantity(foodId: Int): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun upsert(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE foodId = :foodId")
    abstract suspend fun remove(foodId: Int)

    @Query("DELETE FROM cart_items")
    abstract suspend fun clear()

    @Transaction
    open suspend fun addToCart(foodId: Int, quantity: Int) {
        val current = getQuantity(foodId) ?: 0
        upsert(CartItemEntity(foodId = foodId, quantity = current + quantity))
    }

    @Transaction
    open suspend fun setQuantity(foodId: Int, quantity: Int) {
        if (quantity <= 0) remove(foodId) else upsert(CartItemEntity(foodId, quantity))
    }
}
