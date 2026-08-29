package com.ecommerce.ui.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ecommerce.EcommerceApplication
import com.ecommerce.ui.account.AccountViewModel
import com.ecommerce.ui.cart.CartViewModel
import com.ecommerce.ui.checkout.CheckoutViewModel
import com.ecommerce.ui.detail.DetailProductViewModel
import com.ecommerce.ui.katalog.KatalogViewModel
import com.ecommerce.ui.orders.OrdersViewModel
import com.ecommerce.ui.orders.detail.OrderDetailViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            KatalogViewModel(
                productRepository = ecommerceApplication().container.productRepository
            )
        }
        initializer {
            DetailProductViewModel(
                savedStateHandle = createSavedStateHandle(),
                productRepository = ecommerceApplication().container.productRepository,
                cartRepository = ecommerceApplication().container.cartRepository
            )
        }
        initializer {
            CartViewModel(
                cartRepository = ecommerceApplication().container.cartRepository
            )
        }
        initializer {
            CheckoutViewModel(
                cartRepository = ecommerceApplication().container.cartRepository,
                orderRepository = ecommerceApplication().container.orderRepository
            )
        }
        initializer {
            OrdersViewModel(
                orderRepository = ecommerceApplication().container.orderRepository
            )
        }
        initializer {
            OrderDetailViewModel(
                savedStateHandle = createSavedStateHandle(),
                orderRepository = ecommerceApplication().container.orderRepository
            )
        }
        initializer {
            AccountViewModel()
        }
    }
}

fun CreationExtras.ecommerceApplication(): EcommerceApplication = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as EcommerceApplication)