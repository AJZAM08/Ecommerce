package com.ecommerce.data.repository

import com.ecommerce.data.local.dao.UserDao
import com.ecommerce.data.local.entity.UserEntity
import com.ecommerce.domain.entity.User
import com.ecommerce.domain.entity.UserRole
import com.ecommerce.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class RoomAuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {
    override fun getCurrentUser(): Flow<User?> {
        return userDao.getCurrentUserFlow().map { it?.toDomain()}
    }

    override suspend fun login(email: String, password: String): Result<User> {
        val userEntity = userDao.getUserByCredentials(email.trim(), password.trim()) ?: return Result.failure(IllegalArgumentException("Email atau kata sandi salah."))
        userDao.cleanActiveSessions()
        userDao.setActiveSession(userEntity.id)

        return Result.success(userEntity.toDomain())
    }

    override suspend fun register(
        name: String,
        email: String,
        password: String,
        phone: String,
        address: String
    ): Result<User> {
        val existing = userDao.getUserByEmail(email.trim())
        if (existing != null) {
            return Result.failure(IllegalArgumentException("Email sudah terdaftar. silahkan login"))
        }
        val newUser = UserEntity(
            name = name.trim(),
            email = email.trim(),
            passwordHash = password.trim(),
            phone = phone.trim(),
            address = address.trim(),
            role = UserRole.CUSTOMER.name,
            isCurrentSession = true
        )
        userDao.cleanActiveSessions()
        val insertedId = userDao.insertUser(newUser)
        val created = userDao.getUserByEmail(newUser.email)?.toDomain() ?: return Result.failure(IllegalStateException("Gagal membuat akun pengguna."))
        return Result.success(created)
    }

    override suspend fun logout() {
        userDao.cleanActiveSessions()
    }

    override suspend fun isLoggedIn(): Boolean {
        return userDao.getCurrentUserFlow().firstOrNull() != null
    }
}