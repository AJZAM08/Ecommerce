package com.ecommerce.data.repository

import com.ecommerce.data.local.dao.CartDao
import com.ecommerce.data.local.entity.CartItemEntity
import com.ecommerce.domain.entity.CartItem
import com.ecommerce.domain.entity.Product
import com.ecommerce.domain.entity.ProductVariant
import com.ecommerce.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomCartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getCartItemsFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToCart(product: Product, variant: ProductVariant, quantity: Int) {
        val existingItem = cartDao.getCartItem(productId = product.id, variantId = variant.id)
        if (existingItem != null) {
            cartDao.updateQuantity(existingItem.id, existingItem.quantity + quantity)
        } else {
            cartDao.insertCartItem(
                CartItemEntity(
                    productId = product.id,
                    variantId = variant.id,
                    productName = product.name,
                    variantSize = variant.size,
                    price = variant.price,
                    quantity = quantity,
                    imageUrl = product.imageUrl
                )
            )
        }
    }

    override suspend fun updateQuantity(cartItemId: Long, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteCartItem(cartItemId)
        } else {
            cartDao.updateQuantity(cartItemId, quantity)
        }
    }

    override suspend fun removeFromCart(cartItemId: Long) {
        cartDao.deleteCartItem(cartItemId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}