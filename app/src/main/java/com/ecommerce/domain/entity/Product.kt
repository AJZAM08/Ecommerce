package com.ecommerce.domain.entity

data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val category: ProductCategory,
    val benefits: String,
    val composition: String,
    val usage: String,
    val imageUrl: String,
    val variants: List<ProductVariant>
)

enum class ProductCategory {
    PURE_OIL,
    DRIED_SPICE,
    HERBAL_EXTRACT,
}