package com.dev.sirasa.screens.common.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.local.UserModel
import com.dev.sirasa.data.repository.AuthRepository
import com.google.firebase.messaging.FirebaseMessaging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
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
                            val userModel = UserModel(
                                name = response.data?.name ?: "",
                                email = response.data?.email ?: "",
                                nim = response.data?.nim ?: "",
                                token = response.data?.token ?: ""
                            )
                            authRepository.saveSession(userModel)
                            _loginState.value = LoginState.Success
                        } else {
                            _loginState.value = LoginState.Error(response.message ?: "Login gagal")
                        }
                    }
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Terjadi kesalahan")
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

