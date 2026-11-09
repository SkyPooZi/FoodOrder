package com.example.foodorder.data.repository

import androidx.room.withTransaction
import com.example.foodorder.data.local.database.FoodOrderDatabase
import com.example.foodorder.data.local.database.FoodSeed
import com.example.foodorder.data.local.entity.CartItemDetail
import com.example.foodorder.data.local.entity.FoodEntity
import com.example.foodorder.data.local.entity.OrderEntity
import com.example.foodorder.data.local.entity.OrderItemEntity
import com.example.foodorder.data.local.entity.OrderWithItems
import kotlinx.coroutines.flow.Flow

class FoodRepository(private val database: FoodOrderDatabase) {

    private val foodDao = database.foodDao()
    private val cartDao = database.cartDao()
    private val orderDao = database.orderDao()

    val foods: Flow<List<FoodEntity>> = foodDao.observeAll()
    val cartItems: Flow<List<CartItemDetail>> = cartDao.observeCart()
    val orders: Flow<List<OrderWithItems>> = orderDao.observeOrders()

    suspend fun seedIfEmpty() {
        if (foodDao.count() == 0) foodDao.insertAll(FoodSeed.foods)
    }

    suspend fun toggleFavorite(food: FoodEntity) =
        foodDao.setFavorite(food.id, !food.isFavorite)

    suspend fun addToCart(foodId: Int, quantity: Int) = cartDao.addToCart(foodId, quantity)

    suspend fun updateQuantity(foodId: Int, quantity: Int) = cartDao.setQuantity(foodId, quantity)

    suspend fun removeFromCart(foodId: Int) = cartDao.remove(foodId)

    suspend fun placeOrder(address: String, paymentMethod: String): Boolean =
        database.withTransaction {
            val items = cartDao.getCartOnce()
            if (items.isEmpty()) return@withTransaction false

            val subtotal = items.sumOf { it.price * it.quantity }
            val orderId = orderDao.insertOrder(
                OrderEntity(
                    address = address,
                    paymentMethod = paymentMethod,
                    subtotal = subtotal,
                    shipping = SHIPPING_FEE,
                    total = subtotal + SHIPPING_FEE,
                    createdAt = System.currentTimeMillis()
                )
            )
            orderDao.insertItems(
                items.map {
                    OrderItemEntity(
                        orderId = orderId,
                        foodName = it.name,
                        price = it.price,
                        quantity = it.quantity
                    )
                }
            )
            cartDao.clear()
            true
        }

    companion object {
        const val SHIPPING_FEE = 5000
    }
}
