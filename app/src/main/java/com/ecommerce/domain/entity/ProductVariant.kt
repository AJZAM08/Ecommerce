package com.ecommerce.domain.entity

data class ProductVariant(
    val id: Long,
    val productId: Long,
    val size: String,
    val price: Long,
    val stock: Int
)