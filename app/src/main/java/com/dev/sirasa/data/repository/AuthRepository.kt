package com.dev.sirasa.data.repository

import com.dev.sirasa.data.local.UserModel
import com.dev.sirasa.data.local.UserPreference
import com.dev.sirasa.data.remote.response.auth.EmailResetPasswordResponse
import com.dev.sirasa.data.remote.response.auth.EmailVerifiedResponse
import com.dev.sirasa.data.remote.response.auth.LoginResponse
import com.dev.sirasa.data.remote.response.auth.LogoutRequest
import com.dev.sirasa.data.remote.response.auth.LogoutResponse
import com.dev.sirasa.data.remote.response.auth.RefreshTokenResponse
import com.dev.sirasa.data.remote.retrofit.ApiService
import com.dev.sirasa.data.remote.response.auth.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole

    suspend fun register(name: String, email: String, password: String, passwordConfirm: String, nim: String, phoneNumber: String): Flow<RegisterResponse> = flow {
        emit(apiService.register(name, email, password, passwordConfirm, nim, phoneNumber))
    }

    suspend fun login(email: String?, nim: String?, password: String, deviceToken: String): Flow<LoginResponse> = flow {
        emit(apiService.login(email, nim, password, deviceToken))
    }

    suspend fun logout(deviceToken: String?): Flow<LogoutResponse> = flow {
        emit(apiService.logout(LogoutRequest(deviceToken)))
        userPreference.clearSession()
    }

    suspend fun refreshToken(): Flow<RefreshTokenResponse> = flow {
        emit(apiService.refreshToken())
    }

    suspend fun saveSession(userModel: UserModel) {
        userPreference.saveSession(userModel)
    }

    fun getSession(): Flow<UserModel> = userPreference.getSession()

    suspend fun sendEmailPassword(email: String): Flow<EmailResetPasswordResponse> = flow {
        emit(apiService.sendEmailPassword(email))
    }

    suspend fun resetPassword(
        token: String,
        password: String,
        passwordConfirm: String
    ): Flow<EmailResetPasswordResponse> = flow {
        emit(apiService.resetPassword(token, password, passwordConfirm))
    }

    suspend fun sendEmailVerified(email: String): Flow<EmailVerifiedResponse> = flow {
        emit(apiService.sendEmailVerified(email))
    }

    suspend fun ValidateEmail(token: String): Flow<EmailVerifiedResponse> = flow {
        emit(apiService.validateEmail(token))
    }
}
