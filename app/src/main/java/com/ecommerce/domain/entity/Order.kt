package com.ecommerce.domain.entity

data class Order(
    val id: Long,
    val customerName: String,
    val address: String,
    val phone: String,
    val shippingCost: Long,
    val totalPrice: Long,
    val status: OrderStatus,
    val createdAt: Long,
    val items: List<OrderItem>
)

enum class OrderStatus {
    PENDING,
    PROCESSING,
    SHIPPED,
    COMPLETED
}