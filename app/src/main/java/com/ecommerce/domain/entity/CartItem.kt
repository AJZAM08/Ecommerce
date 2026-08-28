package com.ecommerce.domain.entity

data class CartItem(
    val id: Long,
    val productId: Long,
    val variantId: Long,
    val productName: String,
    val variantSize: String,
    val price: Long,
    val quantity: Int,
    val imageUrl: String
)