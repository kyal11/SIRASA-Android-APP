package com.dev.sirasa.data.repository

import com.dev.sirasa.data.remote.retrofit.ApiService
import com.dev.sirasa.data.remote.response.booking.CreateBookingRequest
import com.dev.sirasa.data.remote.response.booking.CreateBookingResponse
import com.dev.sirasa.data.remote.response.booking.BookingUserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun createBooking(request: CreateBookingRequest): Flow<CreateBookingResponse> = flow {
        emit(apiService.createBooking(request))
    }

    fun getHistoryBookingUser():Flow<BookingUserResponse> = flow {
        emit(apiService.getBookingHistory())
    }

    fun getActiveBooking(): Flow<BookingUserResponse> = flow {
        emit(apiService.getBookingActive())
    }

    fun cancelBooking(id: String) : Flow<BookingUserResponse> = flow {
        emit(apiService.cancelBooking(id))
    }

    fun validationBooking(id: String) : Flow<BookingUserResponse> = flow {
        emit(apiService.validationBooking(id))
    }
}