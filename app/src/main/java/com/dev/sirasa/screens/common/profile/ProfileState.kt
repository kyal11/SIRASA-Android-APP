package com.dev.sirasa.screens.common.profile

sealed class ProfileState {
    data object Idle : ProfileState()
    data object Loading : ProfileState()
    data object Success : ProfileState()
    data class Error(val message: String) : ProfileState()
}