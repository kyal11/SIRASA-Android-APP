package com.dev.sirasa.screens.user.room

sealed class RoomsState {
    data object Idle : RoomsState()
    data object Loading : RoomsState()
    data object Success : RoomsState()
    data class Error(val message: String) : RoomsState()
}

sealed class RoomsSlotsState {
    data object Idle : RoomsSlotsState()
    data object Loading : RoomsSlotsState()
    data object Success : RoomsSlotsState()
    data class Error(val message: String) : RoomsSlotsState()
}