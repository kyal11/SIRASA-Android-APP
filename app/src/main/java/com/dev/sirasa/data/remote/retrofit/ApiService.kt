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
import com.dev.sirasa.data.remote.response.booking.BookingValidationResponse
import com.dev.sirasa.data.remote.response.booking.GetBookingsPaginateResponse
import com.dev.sirasa.data.remote.response.crm.SummaryDataResponse
import com.dev.sirasa.data.remote.response.room.CreateRoomResponse
import com.dev.sirasa.data.remote.response.room.DeletedRoomResponse
import com.dev.sirasa.data.remote.response.room.RoomDetailResponse
import com.dev.sirasa.data.remote.response.room.RoomModel
import com.dev.sirasa.data.remote.response.room.RoomResponse
import com.dev.sirasa.data.remote.response.room.RoomWithSlotResponse
import com.dev.sirasa.data.remote.response.user.CreateUser
import com.dev.sirasa.data.remote.response.user.DeleteUserResponse
import com.dev.sirasa.data.remote.response.user.GetAllUsersPaginateResponse
import com.dev.sirasa.data.remote.response.user.GetDetailUserResponse
import com.dev.sirasa.data.remote.response.user.GetUserResponse
import com.dev.sirasa.data.remote.response.user.UpdateAccount
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
        @Field("nim") nim: String?,
        @Field("password") password: String,
        @Field("deviceToken") deviceToken: String,
    ): LoginResponse

    @POST("logout")
    suspend fun logout(): LogoutResponse

    @POST("refresh-token")
    suspend fun refreshToken(): RefreshTokenResponse

    @FormUrlEncoded
    @POST("send-email-reset-password")
    suspend fun sendEmailPassword(
        @Field("email") email: String
    ): EmailResetPasswordResponse

    @FormUrlEncoded
    @POST("reset-password")
    suspend fun resetPassword(
        @Query("token") token: String,
        @Field("password") password: String,
        @Field("passwordConfirm") passwordConfirm: String
    ): EmailResetPasswordResponse

    @FormUrlEncoded
    @POST("send-validate-email")
    suspend fun sendEmailVerified(
        @Field("email") email: String
    ): EmailVerifiedResponse

    @GET("validate-email")
    suspend fun validateEmail(@Query("token") token: String): EmailVerifiedResponse


    // Room & Slot
    @GET("rooms")
    suspend fun getAllRoom(): RoomResponse

    @GET("rooms/{id}")
    suspend fun getRoomDetail(
        @Path("id") id: String,
        @Query("day") day: String? = null
    ): RoomDetailResponse

    @GET("rooms/slots")
    suspend fun getRoomWithSlots(
        @Query("day") day: String
    ): RoomWithSlotResponse

    @POST("rooms/slots")
    suspend fun createRoom(
        @Body roomModel: RoomModel
    ): CreateRoomResponse

    @PUT("rooms/{id}")
    suspend fun updatedRoom(
        @Path("id") id: String,
        @Body roomModel: RoomModel
    ): CreateRoomResponse


    @DELETE("rooms/{id}")
    suspend fun deletedRoom(
        @Path("id") id: String,
    ): DeletedRoomResponse
    // Booking

    @GET("bookings/paginate")
    suspend fun getBookingsPaginate(
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 10,
        @Query("search") search: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
        @Query("status") status: String? = null
    ): GetBookingsPaginateResponse

    @POST("bookings")
    suspend fun createBooking(
        @Body request: CreateBookingRequest
    ): Any

    @POST("bookings/{id}")
    suspend fun createBookingById(
        @Path("id") id: String,
        @Body request: CreateBookingRequest
    ): Any
    @GET("bookings/history")
    suspend fun getBookingHistory(): BookingUserResponse

    @GET("bookings/history/active")
    suspend fun getBookingActive(): BookingUserResponse

    @PUT("bookings/{id}/cancel")
    suspend fun cancelBooking(
        @Path("id") id: String
    ): BookingValidationResponse

    @PUT("bookings/{id}/done")
    suspend fun validationBooking(
        @Path("id") id: String
    ): BookingValidationResponse

    @GET("bookings/summary")
    suspend fun bookingSummary(
        @Query("day") day: String? = null
    ): BookingSummaryResponse

    //Users
    @GET("users/paginate")
    suspend fun getAllUsersPaginate(
        @Query("page") page: Int = 1,
        @Query("perPage") perPage: Int = 10,
        @Query("search") search: String? = null,
        @Query("role") role: String? = null
    ): GetAllUsersPaginateResponse

    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") id: String
    ): GetDetailUserResponse

    @GET("users/detail")
    suspend fun getDetailUser(): GetDetailUserResponse

    @POST("users")
    suspend fun createUsers(
        @Body createUser: CreateUser
    ): GetDetailUserResponse


    @Multipart
    @PUT("users")
    suspend fun updateAccount(
        @Part parts: List<MultipartBody.Part>
    ): GetDetailUserResponse

    @Multipart
    @PUT("users/{id}")
    suspend fun updateUsers(
        @Path("id") id: String,
        @Part parts: List<MultipartBody.Part>
    ): GetDetailUserResponse

    @PUT("users/{id}")
    suspend fun updateUsersVerified(
        @Path("id") id: String,
        @Body updateDate: UpdateAccount
    ): GetDetailUserResponse

    @DELETE("users/{id}")
    suspend fun deleteUsers(
        @Path("id") id: String
    ): DeleteUserResponse

    // Summary
    @GET("crm/summary")
    suspend fun summaryData(): SummaryDataResponse

    // Export
    @GET("bookings/export/excel")
    suspend fun downloadBookingsExcel(
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): Response<ResponseBody>

    @GET("users/export/excel")
    suspend fun downloadUsersExcel(
        @Query("startDate") startDate: String?,
        @Query("endDate") endDate: String?
    ): Response<ResponseBody>

}