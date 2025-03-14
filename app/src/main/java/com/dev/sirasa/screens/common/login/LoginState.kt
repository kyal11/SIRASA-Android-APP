package com.dev.sirasa.screens.common.login

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val destination: String) : LoginState()
    data class Error(val message: String) : LoginState()
}