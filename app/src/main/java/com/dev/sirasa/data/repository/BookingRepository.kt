package com.dev.sirasa.data.repository

import android.util.Log
import com.dev.sirasa.data.remote.retrofit.ApiService
import com.dev.sirasa.data.remote.response.booking.CreateBookingRequest
import com.dev.sirasa.data.remote.response.booking.CreateBookingResponse
import com.dev.sirasa.data.remote.response.booking.BookingUserResponse
import com.dev.sirasa.data.remote.response.booking.RecommendationResponse
import com.dev.sirasa.screens.user.home.BookingResult
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun createBooking(request: CreateBookingRequest): Flow<BookingResult> = flow {
        try {
            Log.d("BookingRepository", "Mengirim permintaan booking...")
            val response = apiService.createBooking(request)
            Log.d("BookingRepository", "Response diterima: $response")

            // Inisialisasi Gson
            val gson = Gson()

            // Mengonversi respons menjadi JsonObject
            val jsonStr = gson.toJson(response)
            val jsonObject = gson.fromJson(jsonStr, JsonObject::class.java)

            // Mendapatkan nilai status
            val status = if (jsonObject.has("status")) jsonObject.get("status").asString else null

            when (status) {
                "success" -> {
                    try {
                        // Konversi langsung ke CreateBookingResponse
                        val bookingResponse = gson.fromJson(jsonStr, CreateBookingResponse::class.java)
                        emit(BookingResult.Success(bookingResponse))
                    } catch (e: Exception) {
                        Log.e("BookingRepository", "Error parsing CreateBookingResponse: ${e.message}", e)
                        emit(BookingResult.Error("Format response tidak sesuai: ${e.message}"))
                    }
                }
                "recommendation" -> {
                    try {
                        // Konversi langsung ke RecommendationResponse
                        val recommendResponse = gson.fromJson(jsonStr, RecommendationResponse::class.java)
                        emit(BookingResult.Recommendation(recommendResponse))
                    } catch (e: Exception) {
                        Log.e("BookingRepository", "Error parsing RecommendationResponse: ${e.message}", e)
                        emit(BookingResult.Error("Format response tidak sesuai: ${e.message}"))
                    }
                }
                else -> {
                    // Menangani kasus status tidak dikenal atau null
                    val message = if (jsonObject.has("message"))
                        jsonObject.get("message").asString
                    else
                        "Status tidak dikenal"
                    Log.e("BookingRepository", "Gagal membuat booking: $message")
                    emit(BookingResult.Error(message))
                }
            }
        } catch (e: Exception) {
            Log.e("BookingRepository", "Error terjadi: ${e.message}", e)
            emit(BookingResult.Error(e.message ?: "Terjadi kesalahan"))
        }
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