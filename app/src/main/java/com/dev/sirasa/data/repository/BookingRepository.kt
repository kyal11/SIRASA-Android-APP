package com.dev.sirasa.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dev.sirasa.data.remote.paging.BookingPagingSource
import com.dev.sirasa.data.remote.paging.UsersPagingSource
import com.dev.sirasa.data.remote.response.booking.BookingSummaryResponse
import com.dev.sirasa.data.remote.retrofit.ApiService
import com.dev.sirasa.data.remote.response.booking.CreateBookingRequest
import com.dev.sirasa.data.remote.response.booking.CreateBookingResponse
import com.dev.sirasa.data.remote.response.booking.BookingUserResponse
import com.dev.sirasa.data.remote.response.booking.BookingValidationResponse
import com.dev.sirasa.data.remote.response.booking.DataBooking
import com.dev.sirasa.data.remote.response.booking.DataBookingsPaginate
import com.dev.sirasa.data.remote.response.booking.RecommendationResponse
import com.dev.sirasa.data.remote.response.user.DataUser
import com.dev.sirasa.screens.user.home.BookingResult
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val apiService: ApiService
) {
    fun createBooking(request: CreateBookingRequest): Flow<BookingResult> = flow {
        try {
            Log.d("BookingRepository", "Mengirim permintaan booking...")
            val response = apiService.createBooking(request)
            Log.d("BookingRepository", "Response diterima: $response")

            val gson = Gson()

            val jsonStr = gson.toJson(response)
            val jsonObject = gson.fromJson(jsonStr, JsonObject::class.java)

            val status = if (jsonObject.has("status")) jsonObject.get("status").asString else null

            when (status) {
                "success" -> {
                    try {
                        val bookingResponse = gson.fromJson(jsonStr, CreateBookingResponse::class.java)
                        emit(BookingResult.Success(bookingResponse))
                    } catch (e: Exception) {
                        Log.e("BookingRepository", "Error parsing CreateBookingResponse: ${e.message}", e)
                        emit(BookingResult.Error("Format response tidak sesuai: ${e.message}"))
                    }
                }
                "recommendation" -> {
                    try {
                        val recommendResponse = gson.fromJson(jsonStr, RecommendationResponse::class.java)
                        emit(BookingResult.Recommendation(recommendResponse))
                    } catch (e: Exception) {
                        Log.e("BookingRepository", "Error parsing RecommendationResponse: ${e.message}", e)
                        emit(BookingResult.Error("Format response tidak sesuai: ${e.message}"))
                    }
                }
                else -> {
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

    fun createBookingById(id: String, request: CreateBookingRequest): Flow<BookingResult> = flow {
        try {
            Log.d("BookingRepository", "Mengirim permintaan booking...")
            val response = apiService.createBookingById(id, request)
            Log.d("BookingRepository", "Response diterima: $response")

            val gson = Gson()

            val jsonStr = gson.toJson(response)
            val jsonObject = gson.fromJson(jsonStr, JsonObject::class.java)

            val status = if (jsonObject.has("status")) jsonObject.get("status").asString else null

            when (status) {
                "success" -> {
                    try {
                        val bookingResponse = gson.fromJson(jsonStr, CreateBookingResponse::class.java)
                        emit(BookingResult.Success(bookingResponse))
                    } catch (e: Exception) {
                        Log.e("BookingRepository", "Error parsing CreateBookingResponse: ${e.message}", e)
                        emit(BookingResult.Error("Format response tidak sesuai: ${e.message}"))
                    }
                }
                "recommendation" -> {
                    try {
                        val recommendResponse = gson.fromJson(jsonStr, RecommendationResponse::class.java)
                        emit(BookingResult.Recommendation(recommendResponse))
                    } catch (e: Exception) {
                        Log.e("BookingRepository", "Error parsing RecommendationResponse: ${e.message}", e)
                        emit(BookingResult.Error("Format response tidak sesuai: ${e.message}"))
                    }
                }
                else -> {
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
    fun getPaginatedBookings(
        searchQuery: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        status: String? = null
    ): Flow<PagingData<DataBookingsPaginate>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { BookingPagingSource(apiService, startDate = startDate, endDate = endDate, status = status, search = searchQuery) }
        ).flow
    }

    fun getHistoryBookingUser():Flow<BookingUserResponse> = flow {
        emit(apiService.getBookingHistory())
    }
    fun getSummaryBooking(day: String? = null):Flow<BookingSummaryResponse> = flow {
        emit(apiService.bookingSummary(day))
    }
    fun getActiveBooking(): Flow<BookingUserResponse> = flow {
        emit(apiService.getBookingActive())
    }

    fun cancelBooking(id: String) : Flow<BookingValidationResponse> = flow {
        emit(apiService.cancelBooking(id))
    }

    fun validationBooking(id: String) : Flow<BookingValidationResponse> = flow {
        emit(apiService.validationBooking(id))
    }
    fun downloadBookingsExcelFlow(startDate: String?, endDate: String?): Flow<Response<ResponseBody>> = flow {
        emit(apiService.downloadBookingsExcel(startDate, endDate))
    }
}