package com.dev.sirasa.screens.common.email_verification

sealed class VerifiedState {
    data object Idle : VerifiedState()
    data object Loading : VerifiedState()
    data object Success : VerifiedState()
    data class Error(val message: String) : VerifiedState()
}