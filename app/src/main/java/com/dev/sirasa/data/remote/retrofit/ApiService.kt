package com.dev.sirasa.data.remote.retrofit

import com.dev.sirasa.data.remote.response.auth.LoginResponse
import com.dev.sirasa.data.remote.response.auth.LogoutResponse
import com.dev.sirasa.data.remote.response.auth.RefreshTokenResponse
import com.dev.sirasa.data.remote.response.auth.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
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
}