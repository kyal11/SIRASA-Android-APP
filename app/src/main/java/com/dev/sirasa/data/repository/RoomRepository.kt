package com.dev.sirasa.data.repository

import com.dev.sirasa.data.remote.response.room.RoomDetailResponse
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

    suspend fun getRoomDetail(id: String, day: String): Flow<RoomDetailResponse> = flow {
        emit(apiService.getRoomDetail(id, day))
    }

    suspend fun getRoomWithSlots(day: String): Flow<RoomWithSlotResponse> = flow {
        emit(apiService.getRoomWithSlots(day))
    }
}