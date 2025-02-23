package com.dev.sirasa.screens.common.forget_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.screens.common.register.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
            authRepository.sendEmailPassword(email)
                .catch { e ->
                    _resetPasswordState.value = ResetPasswordState.Error(e.message ?: "Unknown Error")
                }
                .collectLatest { response ->
                    if (response.status == "success") {
                        _resetPasswordState.value = ResetPasswordState.Success
                    } else {
                        _resetPasswordState.value = ResetPasswordState.Error(response.message ?: "Register gagal")
                    }
                }
        }
    }

    fun resetPassword(token: String, password: String, passwordConfirm: String) {
        viewModelScope.launch {
            _resetPasswordState.value = ResetPasswordState.Loading
            authRepository.resetPassword(token, password, passwordConfirm)
                .catch { e ->
                    _resetPasswordState.value = ResetPasswordState.Error(e.message ?: "Unknown Error")
                }
                .collectLatest { response ->
                    if (response.status == "success") {
                        _resetPasswordState.value = ResetPasswordState.Success
                    } else {
                        _resetPasswordState.value = ResetPasswordState.Error(response.message ?: "Register gagal")
                    }
                }
        }
    }
}
