package com.ecommerce.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.domain.entity.User
import com.ecommerce.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successUser: User? = null
)

class AuthViewModel (
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    fun login(email: String, pass: String, onSuccess: (User) -> Unit) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email dan kata sandi wajib diisi.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result =  authRepository.login(email, pass)
            result.onSuccess { user ->
                _uiState.update { it.copy(isLoading = false, successUser = user) }
                onSuccess(user)
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Gagal masuk.") }
            }
        }
    }

    fun register(
        name: String,
        email: String,
        pass: String,
        phone: String,
        address: String,
        onSuccess: (User) -> Unit
    ) {
        if (name.isBlank() || email.isBlank() || pass.isBlank() || phone.isBlank() || address.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Seluruh kolom pendaftaran wajib diisi") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.register(name, email, pass, phone, address)
            result.onSuccess { user ->
                _uiState.update { it.copy(isLoading = false, successUser = user) }
                onSuccess(user)
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Gagal mendaftar") }
            }
        }
    }
}