package com.ecommerce.data

import com.ecommerce.data.repository.FakeCartRepositoryImpl
import com.ecommerce.data.repository.FakeOrderRepositoryImpl
import com.ecommerce.data.repository.FakeProductRepositoryImpl
import com.ecommerce.domain.repository.CartRepository
import com.ecommerce.domain.repository.OrderRepository
import com.ecommerce.domain.repository.ProductRepository

interface AppContainer {
    val productRepository: ProductRepository
    val cartRepository: CartRepository
    val orderRepository: OrderRepository
}

class DefaultAppContainer : AppContainer {
    override val cartRepository: CartRepository by lazy { FakeCartRepositoryImpl() }
    override val orderRepository: OrderRepository by lazy { FakeOrderRepositoryImpl() }
    override val productRepository: ProductRepository by lazy { FakeProductRepositoryImpl() }
}