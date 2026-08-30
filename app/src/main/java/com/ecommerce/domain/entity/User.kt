package com.ecommerce.domain.entity

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val defaultAddress: String,
    val role: UserRole = UserRole.CUSTOMER
)