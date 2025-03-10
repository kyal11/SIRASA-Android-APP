package com.dev.sirasa.data.remote.retrofit

import com.dev.sirasa.data.remote.response.auth.EmailResetPasswordResponse
import com.dev.sirasa.data.remote.response.auth.EmailVerifiedResponse
import com.dev.sirasa.data.remote.response.auth.LoginResponse
import com.dev.sirasa.data.remote.response.auth.LogoutResponse
import com.dev.sirasa.data.remote.response.auth.RefreshTokenResponse
import com.dev.sirasa.data.remote.response.auth.RegisterResponse
import com.dev.sirasa.data.remote.response.booking.BaseBookingResponse
import com.dev.sirasa.data.remote.response.booking.BookingSummaryResponse
import com.dev.sirasa.data.remote.response.booking.CreateBookingRequest
import com.dev.sirasa.data.remote.response.booking.CreateBookingResponse
import com.dev.sirasa.data.remote.response.booking.BookingUserResponse
import com.dev.sirasa.data.remote.response.room.RoomDetailResponse
import com.dev.sirasa.data.remote.response.room.RoomResponse
import com.dev.sirasa.data.remote.response.room.RoomWithSlotResponse
import com.dev.sirasa.data.remote.response.user.GetDetailUserResponse
import com.dev.sirasa.data.remote.response.user.GetUserResponse
import com.dev.sirasa.data.remote.response.user.UpdateAccount
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // AUTH
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("passwordConfirm") passwordConfirm: String,
        @Field("nim") nim: String,
        @Field("phoneNumber") phoneNumber: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String?,
        @Field("nim") nim:String?,
        @Field("password") password: String,
        @Field("deviceToken") deviceToken: String,
    ) : LoginResponse

    @POST("logout")
    suspend fun logout() : LogoutResponse

    @POST("refresh-token")
    suspend fun refreshToken() : RefreshTokenResponse

    @FormUrlEncoded
    @POST("send-email-reset-password")
    suspend fun sendEmailPassword(
        @Field("email") email: String
    ) : EmailResetPasswordResponse

    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword(
        @Query("token") token: String,
        @Field("password") password: String,
        @Field("passwordConfirm") passwordConfirm: String
    ) : EmailResetPasswordResponse

    @FormUrlEncoded
    @POST("send-validate-email")
    suspend fun sendEmailVerified(
        @Field("email") email: String
    ) : EmailVerifiedResponse

    @GET("validate-email")
    suspend fun validateEmail(@Query("token") token: String): EmailVerifiedResponse


    // Room & Slot
    @GET("rooms")
    suspend fun getAllRoom() : RoomResponse

    @GET("rooms/{id}")
    suspend fun getRoomDetail(
        @Path("id") id: String,
        @Query("day") day: String
    ): RoomDetailResponse

    @GET("rooms/slots")
    suspend fun getRoomWithSlots(
        @Query("day") day: String
    ): RoomWithSlotResponse

    // Booking
    @POST("bookings")
    suspend fun createBooking(
        @Body request: CreateBookingRequest
    ) : Any

    @GET("bookings/history")
    suspend fun getBookingHistory() : BookingUserResponse

    @GET("bookings/history/active")
    suspend fun getBookingActive() : BookingUserResponse

    @PUT("bookings/{id}/cancel")
    suspend fun cancelBooking(
        @Path("id") id: String
    ) : BookingUserResponse

    @PUT("bookings/{id}/done")
    suspend fun validationBooking(
        @Path("id") id: String
    ) : BookingUserResponse

    @GET("bookings/summary")
    suspend fun bookingSummary(
        @Query("day") day: String? = null
    ) : BookingSummaryResponse

    //Users
    @GET("users")
    suspend fun getAllUser() : GetUserResponse

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: String
    ) : GetDetailUserResponse

    @GET("users/detail")
    suspend fun getDetailUser() : GetDetailUserResponse

    @Multipart
    @PUT("users")
    suspend fun updateAccount(
        @Part parts: List<MultipartBody.Part>
    ): GetDetailUserResponse

    @PUT("users/{id}")
    suspend fun updateUsers(
        @Body request: UpdateAccount
    ) : GetDetailUserResponse
}