package com.ecommerce.ui.account

import com.ecommerce.domain.entity.User

data class AccountUiState(
    val user: User = User(
        id = 1L,
        name = "Ahmad Fulan",
        email = "ahmad.fulan@email.com",
        phone = "081234567890",
        defaultAddress = "Jl. Rempah Herbal No. 12, Kel. Sukamaju, Kec. Cilincing, Jakarta Utara 14120"
    ),
    val isEditDialogOpen: Boolean = false,
    val isAboutDialogOpen: Boolean = false
)