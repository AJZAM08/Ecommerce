package com.ecommerce.data.repository

import com.ecommerce.data.local.dao.OrderDao
import com.ecommerce.data.local.entity.OrderEntity
import com.ecommerce.data.local.entity.OrderItemEntity
import com.ecommerce.domain.entity.CartItem
import com.ecommerce.domain.entity.Order
import com.ecommerce.domain.entity.OrderStatus
import com.ecommerce.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomOrderRepositoryImpl(
    private val orderDao: OrderDao
) : OrderRepository {

    override fun getOrders(): Flow<List<Order>> {
        return orderDao.getOrdersFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getOrderById(id: Long): Order? {
        return orderDao.getOrderById(id)?.toDomain()
    }

    override suspend fun createOrder(
        customerName: String,
        address: String,
        phone: String,
        shippingCost: Long,
        items: List<CartItem>
    ): Result<Order> {
        if (items.isEmpty()) {
            return Result.failure(IllegalArgumentException("Keranjang belanja kosong."))
        }

        val subtotal = items.sumOf { it.price * it.quantity }
        val total = subtotal + shippingCost
        val now = System.currentTimeMillis()

        val orderEntity = OrderEntity(
            customerName = customerName,
            address = address,
            phone = phone,
            shippingCost = shippingCost,
            totalPrice = total,
            status = OrderStatus.PENDING.name,
            createdAt = now
        )

        val newOrderId = orderDao.insertOrder(orderEntity)

        val itemEntities = items.map { cartItem ->
            OrderItemEntity(
                orderId = newOrderId,
                productId = cartItem.productId,
                productName = cartItem.productName,
                variantSize = cartItem.variantSize,
                price = cartItem.price,
                quantity = cartItem.quantity
            )
        }

        orderDao.insertOrderItems(itemEntities)

        val createdOrder = orderDao.getOrderById(newOrderId)?.toDomain()
            ?: return Result.failure(IllegalStateException("Gagal mengambil pesanan yang baru dibuat."))

        return Result.success(createdOrder)
    }
    override suspend fun updateOrderStatus(orderId: Long, newStatus: OrderStatus) {
        orderDao.updateOrderStatus(orderId, newStatus.name)
    }
}