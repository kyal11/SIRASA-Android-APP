package com.dev.sirasa.screens.common.email_verification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.screens.common.forget_password.ResetPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifiedViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _verifiedState = MutableStateFlow<VerifiedState>(VerifiedState.Idle)
    val verifiedState: StateFlow<VerifiedState> = _verifiedState

    init {
        sendEmail()
    }

    fun sendEmail() {
        viewModelScope.launch {
            _verifiedState.value = VerifiedState.Loading

            val userSession = authRepository.getSession().firstOrNull()
            val email = userSession?.email ?: ""
            Log.d("VerifiedViewModel", "Sending verification email to: $email")
            if (email.isNotEmpty()) {
                authRepository.sendEmailVerified(email)
                    .catch { e ->
                        Log.e("VerifiedViewModel", "Error sending email: ${e.message}")
                        _verifiedState.value = VerifiedState.Error(e.message ?: "Unknown Error")
                    }
                    .collectLatest { response ->
                        if (response.status == "success") {
                            _verifiedState.value = VerifiedState.Success

                        } else {
                            _verifiedState.value = VerifiedState.Error(response.message ?: "Email verification failed")
                        }
                    }
            } else {
                _verifiedState.value = VerifiedState.Error("Email not found")
            }
        }
    }
}