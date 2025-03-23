package com.dev.sirasa.screens.user.room

import com.dev.sirasa.data.remote.response.room.DataRoomDetail

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

sealed class RoomsDetailState {
    data object Idle : RoomsDetailState()
    data object Loading : RoomsDetailState()
    data class Success(val data: DataRoomDetail) : RoomsDetailState()
    data class Error(val message: String) : RoomsDetailState()
}
sealed class AddRoomState {
    data object Idle : AddRoomState()
    data object Loading : AddRoomState()
    data object Success : AddRoomState()
    data class Error(val message: String) : AddRoomState()
}
sealed class EditRoomState {
    data object Idle : EditRoomState()
    data object Loading : EditRoomState()
    data object Success : EditRoomState()
    data class Error(val message: String) : EditRoomState()
}
sealed class DeleteRoomState {
    data object Idle : DeleteRoomState()
    data object Loading : DeleteRoomState()
    data object Success : DeleteRoomState()
    data class Error(val message: String) : DeleteRoomState()
}
