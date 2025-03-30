package com.dev.sirasa.data.repository

import com.dev.sirasa.data.remote.response.faq.FaqResponse
import com.dev.sirasa.data.remote.response.room.RoomResponse
import com.dev.sirasa.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FaqRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getAllFaq(): Flow<FaqResponse> = flow {
        emit(apiService.getAllFaq())
    }
}