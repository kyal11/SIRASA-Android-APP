package com.dev.sirasa.screens.admin.data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dev.sirasa.data.remote.response.user.CreateUser
import com.dev.sirasa.data.remote.response.user.DataUser
import com.dev.sirasa.data.remote.response.user.UpdateAccount
import com.dev.sirasa.data.repository.UsersRepository
import com.dev.sirasa.screens.common.profile.ProfileState
import com.dev.sirasa.screens.user.home.BookingState
import com.dev.sirasa.screens.user.room.RoomsState
import com.dev.sirasa.utils.ImageCompressor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val usersRepository: UsersRepository
): ViewModel(){
    private val _summaryData = MutableStateFlow<DataSummaryState>(DataSummaryState.Idle)
    val summaryData: StateFlow<DataSummaryState> = _summaryData.asStateFlow()

    private val _usersState = MutableStateFlow<PagingData<DataUser>>(PagingData.empty())
    val usersState: StateFlow<PagingData<DataUser>> = _usersState.asStateFlow()

    private val _addUserState = MutableStateFlow<DataAddUserState>(DataAddUserState.Idle)
    val addUserState: StateFlow<DataAddUserState> = _addUserState.asStateFlow()

    private val _detailUserState = MutableStateFlow<DataUserDetailState>(DataUserDetailState.Idle)
    val detailUserState: StateFlow<DataUserDetailState> = _detailUserState.asStateFlow()

    private val _deleteUserState = MutableStateFlow<DataUserDeleteState>(DataUserDeleteState.Idle)
    val deleteUserState: StateFlow<DataUserDeleteState> = _deleteUserState.asStateFlow()

    private val _updateUserState = MutableStateFlow<UpdateUserState>(UpdateUserState.Idle)
    val updateUserState: StateFlow<UpdateUserState> = _updateUserState
    fun getSummary() {
        viewModelScope.launch {
            _summaryData.value = DataSummaryState.Loading
            usersRepository.getSummary()
                .catch { exception ->
                    _summaryData.value = DataSummaryState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    if (response.status == "success" && response.data != null) {
                        _summaryData.value = DataSummaryState.Success(response.data)
                    } else {
                        _summaryData.value = DataSummaryState.Error(response.message ?: "Gagal memuat data")
                    }
                }
        }
    }

    fun getUsers(search: String? = null, role: String? = null) {
        viewModelScope.launch {
            usersRepository.getPaginatedUsers(search, role)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _usersState.value = pagingData
                }
        }
    }
    fun createUser(createUser: CreateUser) {
        viewModelScope.launch {
            _addUserState.value = DataAddUserState.Loading
            usersRepository.createUser(createUser)
                .catch { exception ->
                    _addUserState.value = DataAddUserState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    _addUserState.value = DataAddUserState.Success(response)
                }
        }
    }
    fun getUserById(userId: String) {
        viewModelScope.launch {
            _detailUserState.value = DataUserDetailState.Loading
            usersRepository.getUserById(userId)
                .catch { exception ->
                    _detailUserState.value = DataUserDetailState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    _detailUserState.value = response.data?.let { DataUserDetailState.Success(it) }!!
                }
        }
    }

    fun deleteUserById(userId: String) {
        viewModelScope.launch {
            _deleteUserState.value = DataUserDeleteState.Loading
            usersRepository.deleteUserById(userId)
                .catch { exception ->
                    _deleteUserState.value = DataUserDeleteState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    _deleteUserState.value = response.data?.let { DataUserDeleteState.Success(it) }!!
                }
        }
    }
    fun updateAccount(userId: String, updateData: UpdateAccount) {
        viewModelScope.launch {
            _updateUserState.value = UpdateUserState.Loading
            usersRepository.updateUserById(userId, updateData)
                .catch { e ->
                    Log.e("ProfileViewModel", "Error updating profile: ${e.message}")
                    _updateUserState.value = UpdateUserState.Error(e.message ?: "Failed to update profile")
                }
                .collectLatest { response ->
                    _updateUserState.value = UpdateUserState.Success
                }
        }
    }

    fun updateProfileImage(userId: String, context: Context, imageUri: Uri) {
        viewModelScope.launch {
            _updateUserState.value = UpdateUserState.Loading
            try {
                val compressedFile = ImageCompressor.compressImage(context, imageUri)
                val updateData = UpdateAccount()

                usersRepository.updateUserById(userId,updateData, compressedFile)
                    .catch { e ->
                        Log.e("ProfileViewModel", "Error updating profile image: ${e.message}")
                        _updateUserState.value = UpdateUserState.Error(e.message ?: "Failed to update profile image")
                    }
                    .collectLatest { response ->
                        _updateUserState.value = UpdateUserState.Success
                    }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error processing image: ${e.message}")
                _updateUserState.value = UpdateUserState.Error("Failed to process image")
            }
        }
    }
}