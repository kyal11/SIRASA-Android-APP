package com.dev.sirasa.data.repository

import com.dev.sirasa.data.local.UserModel
import com.dev.sirasa.data.local.UserPreference
import com.dev.sirasa.data.remote.response.auth.LoginResponse
import com.dev.sirasa.data.remote.response.auth.LogoutResponse
import com.dev.sirasa.data.remote.response.auth.RefreshTokenResponse
import com.dev.sirasa.data.remote.retrofit.ApiService
import com.dev.sirasa.data.remote.response.auth.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun register(name: String, email: String, password: String, passwordConfirm: String, nim: String, phoneNumber: String): Flow<RegisterResponse> = flow {
        emit(apiService.register(name, email, password, passwordConfirm, nim, phoneNumber))
    }

    suspend fun login(email: String?, nim: String?, password: String, deviceToken: String): Flow<LoginResponse> = flow {
        emit(apiService.login(email, nim, password, deviceToken))
    }

    suspend fun logout(): Flow<LogoutResponse> = flow {
        emit(apiService.logout())
    }

    suspend fun refreshToken(): Flow<RefreshTokenResponse> = flow {
        emit(apiService.refreshToken())
    }

    suspend fun saveSession(userModel: UserModel) {
        userPreference.saveSession(userModel)
    }

    fun getSession(): Flow<UserModel> = userPreference.getSession()

    suspend fun clearSession() {
        userPreference.clearSession()
    }
}