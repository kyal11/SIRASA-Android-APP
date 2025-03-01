package com.dev.sirasa.screens.common.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.local.UserModel
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.utils.JsonUtils.extractMessageFromJson
import com.dev.sirasa.utils.JwtUtils
import com.google.firebase.messaging.FirebaseMessaging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlinx.serialization.json.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private var deviceToken: String? = null

    init {
        fetchFCMToken()
    }

    fun login(email: String?, nim: String?, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                deviceToken?.let {
                    authRepository.login(email, nim, password, it).collect { response ->
                        if (response.status == "success") {
                            val token = response.data?.token ?: ""
                            Log.d("token verified: ", token)
                            val isVerified = JwtUtils.isVerified(token) ?: false
                            val role = JwtUtils.getRole(token) ?: false

                            val userModel = UserModel(
                                name = response.data?.name ?: "",
                                email = response.data?.email ?: "",
                                nim = response.data?.nim ?: "",
                                token = token
                            )
                            authRepository.saveSession(userModel)
                            Log.d("verified: ", "$isVerified")
                            val destination = when {
                                role in listOf("superadmin", "admin") -> "main_screen_admin"
                                !isVerified -> "verified_account"
                                else -> "main_screen_user"
                            }

                            _loginState.value = LoginState.Success(destination)
                        } else {
                            _loginState.value = LoginState.Error(response.message ?: "Login gagal")
                        }
                    }
                } ?: run {
                    _loginState.value = LoginState.Error("Device token tidak tersedia")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage =  extractMessageFromJson(errorBody) ?: "HTTP error occurred"

                Log.e("LoginViewModel", "HTTP Error: $errorMessage", e)
                _loginState.value = LoginState.Error(errorMessage)
            } catch (e: IOException) {
                Log.e("LoginViewModel", "Network Error", e)
                _loginState.value = LoginState.Error("Koneksi gagal. Periksa koneksi internet Anda.")
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Unknown Error", e)
                _loginState.value = LoginState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    private fun fetchFCMToken() {
        getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deviceToken = task.result
                Log.d("FCM Token", "Token: $deviceToken")
            } else {
                Log.w("FCM Token", "Fetching FCM token failed", task.exception)
            }
        }
    }
}
