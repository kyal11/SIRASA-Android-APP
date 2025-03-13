package com.dev.sirasa.screens.user.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.local.UserPreference
import com.dev.sirasa.data.remote.WebSocketManager
import com.dev.sirasa.data.remote.response.booking.DataHistory
import com.dev.sirasa.data.remote.response.room.DataRoom
import com.dev.sirasa.data.remote.response.room.DataRoomDetail
import com.dev.sirasa.data.repository.BookingRepository
import com.dev.sirasa.screens.user.home.RoomDetailState
import com.dev.sirasa.screens.user.room.RoomsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HistoryMainModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val userPreference: UserPreference
) : ViewModel() {
    private val _history= MutableStateFlow<List<DataHistory>>(emptyList())
    val history: StateFlow<List<DataHistory>> = _history.asStateFlow()

    private val _historyActive = MutableStateFlow<List<DataHistory>>(emptyList())
    val historyActive: StateFlow<List<DataHistory>> = _historyActive.asStateFlow()

    private val _historyState = MutableStateFlow<HistoryBookingState>(HistoryBookingState.Idle)
    val historyState: StateFlow<HistoryBookingState> = _historyState.asStateFlow()

    private val _activeState = MutableStateFlow<HistoryActiveState>(HistoryActiveState.Idle)
    val activeState: StateFlow<HistoryActiveState> = _activeState.asStateFlow()

    private val _cancelState = MutableStateFlow<CancelBookingState>(CancelBookingState.Idle)
    val cancelState: StateFlow<CancelBookingState> = _cancelState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        viewModelScope.launch {
            WebSocketManager.connect { userPreference.getSession() }
        }
        viewModelScope.launch {
            WebSocketManager.bookingUpdateFlow.collect {
                refreshHistory()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        WebSocketManager.disconnect()
    }

    fun refreshHistory() {
        viewModelScope.launch {
            _isRefreshing.value = true
            getHistoryUser()
            getActiveBooking()
            delay(300)
            _isRefreshing.value = false
        }
    }
    fun getHistoryUser() {
        viewModelScope.launch {
            _historyState.value = HistoryBookingState.Loading
            bookingRepository.getHistoryBookingUser()
                .catch { exception ->
                    _historyState.value = HistoryBookingState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    if (response.status == "success" && response.data != null) {
                        _history.value = response.data.filterNotNull()
                        _historyState.value = HistoryBookingState.Success
                    } else {
                        _historyState.value = HistoryBookingState.Error(response.message ?: "Gagal memuat riwayat peminjaman")
                    }
                }
        }
    }

    fun getActiveBooking() {
        viewModelScope.launch {
            _activeState.value = HistoryActiveState.Loading
            bookingRepository.getActiveBooking()
                .catch { exception ->
                    _activeState.value = HistoryActiveState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    if (response.status == "success" && response.data != null) {
                        _historyActive.value = response.data.filterNotNull()
                        _activeState.value = HistoryActiveState.Success
                    } else {
                        _activeState.value = HistoryActiveState.Error(response.message ?: "Gagal memuat peminjaman aktif")
                    }
                }
        }
    }

    fun cancelBooking(id: String) {
        // Set cancellation state to loading
        _cancelState.value = CancelBookingState.Loading(id)

        // Update UI optimistically - mark the booking as "cancelling" immediately
        updateBookingStatusLocally(id, "cancelling")

        viewModelScope.launch {
            bookingRepository.cancelBooking(id)
                .catch { exception ->
                    // Revert optimistic update if there's an error
                    revertOptimisticUpdate(id)
                    _cancelState.value = CancelBookingState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    if (response.status == "success") {
                        // Update the lists immediately with the confirmed cancellation
                        updateBookingStatusLocally(id, "cancel")
                        _cancelState.value = CancelBookingState.Success

                        // Then refresh from server to ensure data consistency
                        getHistoryUser()
                        getActiveBooking()
                    } else {
                        // Revert optimistic update if cancellation failed
                        revertOptimisticUpdate(id)
                        _cancelState.value = CancelBookingState.Error(response.message ?: "Gagal membatalkan booking")
                    }
                }
        }
    }

    // Helper method to update booking status locally (optimistic update)
    private fun updateBookingStatusLocally(bookingId: String, newStatus: String) {
        // Update in active bookings list
        _historyActive.value = _historyActive.value.map { booking ->
            if (booking.id == bookingId) {
                // Create a copy of the booking with the new status
                booking.copy(status = newStatus)
            } else {
                booking
            }
        }

        // Also update in the history list if it exists there
        _history.value = _history.value.map { booking ->
            if (booking.id == bookingId) {
                booking.copy(status = newStatus)
            } else {
                booking
            }
        }
    }

    // Helper method to revert optimistic updates if cancellation fails
    private fun revertOptimisticUpdate(bookingId: String) {
        getHistoryUser()
        getActiveBooking()
    }
}