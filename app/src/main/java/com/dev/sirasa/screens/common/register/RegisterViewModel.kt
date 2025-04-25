package com.dev.sirasa.screens.common.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.screens.common.forget_password.ResetPasswordState
import com.dev.sirasa.screens.common.login.LoginState
import com.dev.sirasa.utils.JsonUtils.extractMessageFromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun register(
        name: String,
        email: String,
        password: String,
        passwordConfirm: String,
        nim: String,
        phoneNumber: String
    ) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                authRepository.register(name, email, password, passwordConfirm, nim, phoneNumber)
                    .collectLatest { response ->
                        if (response.status == "success") {
                            _registerState.value = RegisterState.Success
                        } else {
                            _registerState.value = RegisterState.Error(response.message ?: "Registrasi gagal")
                        }
                    }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage =  extractMessageFromJson(errorBody) ?: "HTTP error occurred"
                Log.e("RegisterViewModel", "HTTP Error: $errorMessage", e)
                _registerState.value = RegisterState.Error(errorMessage)
            } catch (e: IOException) {
                Log.e("RegisterViewModel", "Network Error", e)
                _registerState.value = RegisterState.Error("Koneksi gagal. Periksa koneksi internet Anda.")
            } catch (e: Exception) {
                Log.e("RegisterViewModel", "Unknown Error", e)
                _registerState.value = RegisterState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }
}