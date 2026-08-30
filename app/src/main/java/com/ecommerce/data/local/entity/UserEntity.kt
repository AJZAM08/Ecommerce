package com.ecommerce.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ecommerce.domain.entity.User
import com.ecommerce.domain.entity.UserRole

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val email: String,
    val passwordHash: String,
    val phone: String,
    val address: String,
    val role: String,
    val isCurrentSession: Boolean = false
) {
    fun toDomain(): User = User(
        id,
        name,
        email,
        phone,
        address,
        role = try {
            UserRole.valueOf(role)
        } catch (e: Exception) {
            UserRole.CUSTOMER
        }
    )
}