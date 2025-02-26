package com.dev.sirasa.data.repository

import com.dev.sirasa.data.remote.retrofit.ApiService
import com.dev.sirasa.data.remote.response.booking.CreateBookingRequest
import com.dev.sirasa.data.remote.response.booking.CreateBookingResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun createBooking(request: CreateBookingRequest): Flow<CreateBookingResponse> = flow {
        emit(apiService.createBooking(request))
    }
}