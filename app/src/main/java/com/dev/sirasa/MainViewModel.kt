package com.dev.sirasa

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.local.UserModel
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.utils.JwtUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole
    // Di MainViewModel, tambahkan:
    private val _deepLinkUri = MutableStateFlow<Uri?>(null)
    val deepLinkUri = _deepLinkUri.asStateFlow()

    fun processDeepLink(uri: Uri?) {
        _deepLinkUri.value = uri
    }
    fun validateEmail(token: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            authRepository.ValidateEmail(token)
                .catch {
                    onResult(false)
                }
                .collect { response ->
                    if (response.status == "success") {
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                }
        }
    }
    fun checkUserSession() {
        viewModelScope.launch {
            authRepository.getSession()
                .catch {
                    _authState.value = AuthState.Unauthorized
                }
                .collect { user ->
                    Log.d("session user", "$user")
                    if (user.token.isNotEmpty()) {
                        refreshUserToken(user)
                    } else {
                        _authState.value = AuthState.Unauthorized
                    }
                }
        }
    }
    private fun refreshUserToken(user: UserModel) {
        viewModelScope.launch {
            authRepository.refreshToken()
                .catch {
                    Log.e("token", "Failed to refresh token")
                    _authState.value = AuthState.Unauthorized
                }
                .collect { response ->
                    if (response.status == "success") {
                        val newToken = response.data?.token ?: ""
                        Log.d("token", "Token refreshed successfully: $newToken")

                        val updatedUser = user.copy(token = newToken)
                        authRepository.saveSession(updatedUser)

                        authenticateUser(newToken)
                    } else {
                        Log.e("token", "Refresh token failed")
                        _authState.value = AuthState.Unauthorized
                    }
                }
        }
    }

    private fun authenticateUser(token: String) {
        val role = JwtUtils.getRole(token)
        val isVerified = JwtUtils.isVerified(token) ?: false
        _userRole.value = role
        _authState.value = when {
            role == "superadmin" || role == "admin" -> AuthState.Authorized("main_screen_admin")
            !isVerified -> AuthState.Unauthorized
            else -> AuthState.Authorized("main_screen_user")
        }
    }
    fun setUserRole(role: String?) {
        _userRole.value = role
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Unauthorized : AuthState()
    data class Authorized(val route: String) : AuthState()
}
