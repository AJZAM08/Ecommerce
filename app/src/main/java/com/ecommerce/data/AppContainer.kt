package com.ecommerce.data

import android.content.Context
import com.ecommerce.data.local.AppDatabase
import com.ecommerce.data.repository.RoomAuthRepositoryImpl
import com.ecommerce.data.repository.RoomCartRepositoryImpl
import com.ecommerce.data.repository.RoomOrderRepositoryImpl
import com.ecommerce.data.repository.RoomProductRepositoryImpl
import com.ecommerce.domain.repository.AuthRepository
import com.ecommerce.domain.repository.CartRepository
import com.ecommerce.domain.repository.OrderRepository
import com.ecommerce.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

interface AppContainer {
    val productRepository: ProductRepository
    val cartRepository: CartRepository
    val orderRepository: OrderRepository
    val authRepository: AuthRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context, applicationScope)
    }
    override val productRepository: ProductRepository by lazy {
        RoomProductRepositoryImpl(database.productDao())
    }
    override val cartRepository: CartRepository by lazy {
        RoomCartRepositoryImpl(database.cartDao())
    }
    override val orderRepository: OrderRepository by lazy {
        RoomOrderRepositoryImpl(database.orderDao())
    }
    override val authRepository: AuthRepository by lazy {
        RoomAuthRepositoryImpl(database.userDao())
    }
}