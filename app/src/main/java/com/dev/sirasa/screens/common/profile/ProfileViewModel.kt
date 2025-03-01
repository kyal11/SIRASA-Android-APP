package com.dev.sirasa.screens.common.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.screens.common.email_verification.VerifiedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: MutableStateFlow<ProfileState> = _profileState


    fun logout() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            authRepository.logout()
                .catch { e ->
                    Log.e("ProfileViewModel", "Error logging out: ${e.message}")
                    _profileState.value = ProfileState.Error(e.message ?: "Unknown Error")
                }
                .collectLatest { response ->
                    if (response.status == "success") {
                        _profileState.value = ProfileState.Success
                    } else {
                        _profileState.value = ProfileState.Error(response.message ?: "Logout failed")
                    }
                }
        }
    }

}