package com.dev.sirasa.screens.user.home

sealed class RoomDetailState {
    data object Idle : RoomDetailState()
    data object Loading : RoomDetailState()
    data object Success : RoomDetailState()
    data class Error(val message: String) : RoomDetailState()
}

sealed class BookingState {
    data object Idle : BookingState()
    data object Loading : BookingState()
    data class Success(val message: String) : BookingState()
    data class Error(val message: String) : BookingState()
}