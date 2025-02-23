package com.dev.sirasa

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.utils.JwtUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            authRepository.getSession()
                .catch {
                    _authState.value = AuthState.Unauthorized
                }
                .collect { user ->
                    if (user.token.isNotEmpty() && !JwtUtils.isTokenExpired(user.token)) {
                        val role = JwtUtils.getRole(user.token)
                        val isVerified = JwtUtils.isVerified(user.token) ?: false
                        _authState.value = when {
                            role == "superadmin" || role == "admin" -> AuthState.Authorized("main_screen_admin")
                            !isVerified -> AuthState.Unauthorized
                            else -> AuthState.Authorized("main_screen_user")
                        }
                    } else {
                        _authState.value = AuthState.Unauthorized
                    }
                }
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Unauthorized : AuthState()
    data class Authorized(val route: String) : AuthState()
}
