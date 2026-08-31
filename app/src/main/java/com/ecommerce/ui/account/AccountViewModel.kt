package com.ecommerce.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecommerce.domain.entity.User
import com.ecommerce.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountUiState(
    val isEditDialogOpen: Boolean = false,
    val isAboutDialogOpen: Boolean = false
)

class AccountViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.getCurrentUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun showEditDialog(show: Boolean) {
        _uiState.update { it.copy(isEditDialogOpen = show) }
    }

    fun showAboutDialog(show: Boolean) {
        _uiState.update { it.copy(isAboutDialogOpen = show) }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}