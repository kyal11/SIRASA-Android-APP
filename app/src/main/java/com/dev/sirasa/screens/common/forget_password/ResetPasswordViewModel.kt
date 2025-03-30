package com.dev.sirasa.screens.common.forget_password

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.screens.common.login.LoginState
import com.dev.sirasa.screens.common.register.RegisterState
import com.dev.sirasa.utils.JsonUtils.extractMessageFromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _resetPasswordState = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Idle)
    val resetPasswordState: StateFlow<ResetPasswordState> = _resetPasswordState
    fun sendEmail(email: String) {
        viewModelScope.launch {
            _resetPasswordState.value = ResetPasswordState.Loading
            try {
                authRepository.sendEmailPassword(email)
                    .collectLatest { response ->
                        if (response.status == "success") {
                            _resetPasswordState.value = ResetPasswordState.Success
                        } else {
                            _resetPasswordState.value = ResetPasswordState.Error(response.message ?: "Gagal mengirim email reset password")
                        }
                    }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = extractMessageFromJson(errorBody) ?: "Terjadi kesalahan HTTP"
                Log.e("ResetPasswordViewModel", "HTTP Error: $errorMessage", e)
                _resetPasswordState.value = ResetPasswordState.Error(errorMessage)
            } catch (e: Exception) {
                Log.e("ResetPasswordViewModel", "Error: ${e.message}", e)
                _resetPasswordState.value = ResetPasswordState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun resetPassword(token: String, password: String, passwordConfirm: String) {
        viewModelScope.launch {
            _resetPasswordState.value = ResetPasswordState.Loading
            try {
                authRepository.resetPassword(token, password, passwordConfirm)
                    .collectLatest { response ->
                        if (response.status == "success") {
                            _resetPasswordState.value = ResetPasswordState.Success
                        } else {
                            _resetPasswordState.value = ResetPasswordState.Error(response.message ?: "Gagal mereset password")
                        }
                    }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = extractMessageFromJson(errorBody) ?: "Terjadi kesalahan HTTP"
                Log.e("ResetPasswordViewModel", "HTTP Error: $errorMessage", e)
                _resetPasswordState.value = ResetPasswordState.Error(errorMessage)
            } catch (e: Exception) {
                Log.e("ResetPasswordViewModel", "Error: ${e.message}", e)
                _resetPasswordState.value = ResetPasswordState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}
