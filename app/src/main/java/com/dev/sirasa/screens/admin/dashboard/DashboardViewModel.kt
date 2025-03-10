package com.dev.sirasa.screens.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {
    private val _dashboardState = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val dashboardState: StateFlow<DashboardState> = _dashboardState

    fun getSummaryBooking(day: String? = null) {
        viewModelScope.launch {
            _dashboardState.value = DashboardState.Loading
            try {
                bookingRepository.getSummaryBooking(day)
                    .collectLatest { response ->
                        if (response.status == "success") {
                            _dashboardState.value = DashboardState.Success(response)
                        } else {
                            _dashboardState.value = DashboardState.Error(response.message ?: "Gagal mengambil data")
                        }
                    }
            } catch (e: Exception) {
                _dashboardState.value = DashboardState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }
}