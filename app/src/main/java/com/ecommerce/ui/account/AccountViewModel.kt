package com.ecommerce.ui.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AccountViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun showEditDialog(show: Boolean) {
        _uiState.update { it.copy(isEditDialogOpen = show) }
    }

    fun showAboutDialog(show: Boolean) {
        _uiState.update { it.copy(isAboutDialogOpen = show) }
    }

    fun updateUserProfile(name: String, email: String, phone: String, address: String) {
        _uiState.update { currentState ->
            currentState.copy(
                user = currentState.user.copy(
                    name = name,
                    email = email,
                    phone = phone,
                    defaultAddress = address
                ),
                isEditDialogOpen = false
            )
        }
    }
}