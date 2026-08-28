package com.ecommerce.domain.entity

data class OrderItem(
    val id: Long,
    val orderId: Long,
    val productId: Long,
    val productName: String,
    val variantSize: String,
    val price: Long,
    val quantity: Int
)