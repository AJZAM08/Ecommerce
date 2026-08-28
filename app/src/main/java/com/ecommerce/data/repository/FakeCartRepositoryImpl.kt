package com.ecommerce.data.repository

import com.ecommerce.domain.entity.CartItem
import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductVariant
import com.ecommerce.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeCartRepositoryImpl : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    private var nextId = 1L

    override fun getCartItems(): Flow<List<CartItem>> = _cartItems.asStateFlow()
    override suspend fun addToCart(product: Product, variant: ProductVariant, quantity: Int) {
        _cartItems.update { currentList ->
            val existingItemIndex = currentList.indexOfFirst { it.productId == product.id && it.variantId == variant.id }
            if (existingItemIndex != -1) {
                currentList.mapIndexed { index, item ->
                    if (index == existingItemIndex) {
                        item.copy(quantity = item.quantity + quantity)
                    } else item
                }
            } else {
                currentList + CartItem(
                    id = nextId++,
                    product.id,
                    variant.id,
                    product.name,
                    variant.size,
                    variant.price,
                    quantity,
                    product.imageUrl
                )
            }
        }
    }

    override suspend fun updateQuantity(cartItemId: Long, quantity: Int) {
        _cartItems.update { currentList ->
            if (quantity <= 0) {
                currentList.filterNot { it.id == cartItemId }
            } else {
                currentList.map { item ->
                    if (item.id == cartItemId) item.copy(quantity = quantity) else item
                }
            }
        }
    }

    override suspend fun removeFromCart(cartItemId: Long) {
        _cartItems.update { currentList ->
            currentList.filterNot { it.id == cartItemId }
        }
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyList()
    }
}