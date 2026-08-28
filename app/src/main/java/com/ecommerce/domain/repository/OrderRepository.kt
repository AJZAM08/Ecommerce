package com.ecommerce.domain.repository

import com.ecommerce.domain.entity.CartItem
import com.ecommerce.domain.entity.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrders(): Flow<List<Order>>
    suspend fun getOrderById(id: Long): Order?
    suspend fun createOrder(
        customerName: String,
        address: String,
        phone: String,
        shippingCost: Long,
        items: List<CartItem>
    ): Result<Order>
}