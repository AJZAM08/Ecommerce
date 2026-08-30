package com.ecommerce.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ecommerce.domain.entity.CartItem

@Entity(
    tableName = "cart_items"
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val productId: Long,
    val variantId: Long,
    val productName: String,
    val variantSize: String,
    val price: Long,
    val quantity: Int,
    val imageUrl: String
) {
    fun toDomain(): CartItem = CartItem(
        id,
        productId,
        variantId,
        productName,
        variantSize,
        price,
        quantity,
        imageUrl
    )
}