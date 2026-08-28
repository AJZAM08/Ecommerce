package com.ecommerce.data.repository

import com.ecommerce.domain.entity.CartItem
import com.ecommerce.domain.entity.Order
import com.ecommerce.domain.entity.OrderItem
import com.ecommerce.domain.entity.OrderStatus
import com.ecommerce.domain.repository.OrderRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeOrderRepositoryImpl : OrderRepository {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    private var nextOrderId = 1001L

    override fun getOrders(): Flow<List<Order>> = _orders.asStateFlow()
    override suspend fun getOrderById(id: Long): Order? {
        delay(200)
        return _orders.value.find { it.id == id }
    }

    override suspend fun createOrder(
        customerName: String,
        address: String,
        phone: String,
        shippingCost: Long,
        items: List<CartItem>
    ): Result<Order> {
        delay(800)
        if (items.isEmpty()) {
            return Result.failure(IllegalArgumentException("Keranjang belanja kosong"))
        }

        val subtotal = items.sumOf { it.price * it.quantity }
        val total = subtotal + shippingCost
        val newOrder = Order(
            id = nextOrderId++,
            customerName = customerName,
            address,
            phone,
            shippingCost,
            total,
            OrderStatus.PENDING,
            createdAt = System.currentTimeMillis(),
            items = items.mapIndexed { index, cartItem ->
                OrderItem(
                    id = index + 1L,
                    nextOrderId,
                    productId = cartItem.productId,
                    productName = cartItem.productName,
                    variantSize = cartItem.variantSize,
                    price = cartItem.price,
                    quantity = cartItem.quantity
                )
            }
        )
        _orders.update { listOf(newOrder) + it }
        return Result.success(newOrder)
    }
}