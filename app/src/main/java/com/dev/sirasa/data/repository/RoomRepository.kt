package com.dev.sirasa.data.repository

import com.dev.sirasa.data.remote.response.room.CreateRoomResponse
import com.dev.sirasa.data.remote.response.room.DeletedRoomResponse
import com.dev.sirasa.data.remote.response.room.RoomDetailResponse
import com.dev.sirasa.data.remote.response.room.RoomModel
import com.dev.sirasa.data.remote.response.room.RoomResponse
import com.dev.sirasa.data.remote.response.room.RoomWithSlotResponse
import com.dev.sirasa.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllRooms(): Flow<RoomResponse> = flow {
        emit(apiService.getAllRoom())
    }

    suspend fun getRoomDetail(id: String, day: String? = null): Flow<RoomDetailResponse> = flow {
        emit(apiService.getRoomDetail(id, day))
    }

    suspend fun getRoomWithSlots(day: String): Flow<RoomWithSlotResponse> = flow {
        emit(apiService.getRoomWithSlots(day))
    }
    suspend fun createRoom(roomModel: RoomModel): Flow<CreateRoomResponse> = flow {
        emit(apiService.createRoom(roomModel))
    }

    suspend fun updateRoom(id: String, roomModel: RoomModel): Flow<CreateRoomResponse> = flow {
        emit(apiService.updatedRoom(id, roomModel))
    }
    suspend fun deletedRoom(id: String): Flow<DeletedRoomResponse> = flow {
        emit(apiService.deletedRoom(id))
    }
}