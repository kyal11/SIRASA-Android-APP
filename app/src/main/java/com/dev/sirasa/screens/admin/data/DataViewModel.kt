package com.dev.sirasa.screens.admin.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dev.sirasa.data.remote.response.user.DataUser
import com.dev.sirasa.data.repository.UsersRepository
import com.dev.sirasa.screens.user.home.BookingState
import com.dev.sirasa.screens.user.room.RoomsState
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
}