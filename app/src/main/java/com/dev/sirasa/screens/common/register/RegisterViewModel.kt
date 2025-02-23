package com.dev.sirasa.screens.common.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.screens.common.forget_password.ResetPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
                    .catch { e ->
                        _registerState.value = RegisterState.Error(e.message ?: "Unknown Error")
                    }
                    .collectLatest { response ->
                        if (response.status == "success") {
                            _registerState.value = RegisterState.Success
                        } else {
                            _registerState.value = RegisterState.Error(response.message ?: "Register gagal")
                        }
                    }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Terjadi kesalahan saat registrasi")
            }
        }
    }
}