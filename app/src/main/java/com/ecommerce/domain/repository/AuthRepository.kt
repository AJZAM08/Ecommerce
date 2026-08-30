package com.ecommerce.domain.repository

import com.ecommerce.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): Flow<User?>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(
        name: String,
        email: String,
        password: String,
        phone: String,
        address: String
    ): Result<User>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}