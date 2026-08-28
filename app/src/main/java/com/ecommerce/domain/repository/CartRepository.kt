package com.ecommerce.domain.repository

import com.ecommerce.domain.entity.CartItem
import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductVariant
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(product: Product, variant: ProductVariant, quantity: Int)
    suspend fun updateQuantity(cartItemId: Long, quantity: Int)
    suspend fun removeFromCart(cartItemId: Long)
    suspend fun clearCart()
}