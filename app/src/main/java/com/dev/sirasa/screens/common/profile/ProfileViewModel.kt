package com.dev.sirasa.screens.common.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.remote.response.user.DataUser
import com.dev.sirasa.data.remote.response.user.UpdateAccount
import com.dev.sirasa.data.repository.AuthRepository
import com.dev.sirasa.data.repository.UsersRepository
import com.dev.sirasa.utils.ImageCompressor
import com.google.firebase.messaging.FirebaseMessaging.getInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState

    private val _userData = MutableStateFlow<DataUser?>(null)
    val userData: StateFlow<DataUser?> = _userData

    private var deviceToken: String? = null

    init {
        fetchUserProfile()
        fetchFCMToken()
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            usersRepository.getDetailAccount()
                .catch { e ->
                    Log.e("ProfileViewModel", "Error fetching user profile: ${e.message}")
                    _profileState.value = ProfileState.Error(e.message ?: "Failed to fetch profile")
                }
                .collectLatest { response ->
                    _userData.value = response.data
//                    _profileState.value = ProfileState.Success("Update Success")
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            deviceToken?.let {
                authRepository.logout(deviceToken)
//                    .catch { e ->
//                        Log.e("ProfileViewModel", "Error logging out: ${e.message}")
//                        _profileState.value = ProfileState.Error(e.message ?: "Unknown Error")
//                    }
                    .collectLatest { response ->
                        if (response.status == "success") {
                            _profileState.value = ProfileState.Success("Logout Success")
                        }
//                        else {
//                            _profileState.value =
//                                ProfileState.Error(response.message ?: "Logout failed")
//                        }
                    }
            }
        }
    }

    fun updateAccount(updateData: UpdateAccount) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            usersRepository.updateAccount(updateData)
                .catch { e ->
                    Log.e("ProfileViewModel", "Error updating profile: ${e.message}")
                    _profileState.value = ProfileState.Error(e.message ?: "Failed to update profile")
                }
                .collectLatest { response ->
                    _userData.value = response.data
                    _profileState.value = ProfileState.Success("Update Success")
                }
        }
    }
    fun updateProfileImage(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val compressedFile = ImageCompressor.compressImage(context, imageUri)
                val updateData = UpdateAccount()

                usersRepository.updateAccount(updateData, compressedFile)
                    .catch { e ->
                        Log.e("ProfileViewModel", "Error updating profile image: ${e.message}")
                        _profileState.value = ProfileState.Error(e.message ?: "Failed to update profile image")
                    }
                    .collectLatest { response ->
                        _userData.value = response.data
                        _profileState.value = ProfileState.Success("Update Success")
                    }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error processing image: ${e.message}")
                _profileState.value = ProfileState.Error("Failed to process image")
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
