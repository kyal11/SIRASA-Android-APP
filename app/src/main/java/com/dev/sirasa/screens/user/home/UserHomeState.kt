package com.dev.sirasa.screens.user.home

import com.dev.sirasa.data.remote.response.booking.CreateBookingResponse
import com.dev.sirasa.data.remote.response.booking.DataBooking
import com.dev.sirasa.data.remote.response.booking.DataRecommendation
import com.dev.sirasa.data.remote.response.booking.RecommendationResponse

sealed class RoomDetailState {
    data object Idle : RoomDetailState()
    data object Loading : RoomDetailState()
    data object Success : RoomDetailState()
    data class Error(val message: String) : RoomDetailState()
}

sealed class BookingState {
    data object Idle : BookingState()
    data object Loading : BookingState()
    data class Recommendation(val data: List<DataRecommendation?>?, val message: String) : BookingState()
    data class Success(val data: DataBooking) : BookingState()
    data class Error(val message: String) : BookingState()
}
sealed class BookingResult {
    data class Success(val response: CreateBookingResponse) : BookingResult()
    data class Recommendation(val response: RecommendationResponse) : BookingResult()
    data class Error(val message: String) : BookingResult()
}
