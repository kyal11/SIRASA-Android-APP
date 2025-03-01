package com.dev.sirasa.screens.user.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.remote.response.booking.CreateBookingRequest
import com.dev.sirasa.data.remote.response.room.DataRoom
import com.dev.sirasa.data.remote.response.room.DataRoomDetail
import com.dev.sirasa.data.repository.BookingRepository
import com.dev.sirasa.data.repository.RoomRepository
import com.dev.sirasa.screens.user.room.RoomsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val roomRepository: RoomRepository,
    private val bookingRepository: BookingRepository,
    ) : ViewModel() {

    private val _rooms = MutableStateFlow<List<DataRoom>>(emptyList())
    val rooms: StateFlow<List<DataRoom>> = _rooms.asStateFlow()

    private val _roomDetail = MutableStateFlow<DataRoomDetail?>(null)
    val roomDetail: StateFlow<DataRoomDetail?> = _roomDetail.asStateFlow()

    private val _roomsState = MutableStateFlow<RoomsState>(RoomsState.Idle)
    val roomsState: StateFlow<RoomsState> = _roomsState.asStateFlow()

    private val _roomDetailState = MutableStateFlow<RoomDetailState>(RoomDetailState.Idle)
    val roomDetailState: StateFlow<RoomDetailState> = _roomDetailState.asStateFlow()

    private val _bookingState = MutableStateFlow<BookingState>(BookingState.Idle)
    val bookingState: StateFlow<BookingState> = _bookingState.asStateFlow()

    fun getAllRooms() {
        viewModelScope.launch {
            _roomsState.value = RoomsState.Loading
            roomRepository.getAllRooms()
                .catch { exception ->
                    _roomsState.value = RoomsState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    if (response.status == "success" && response.data != null) {
                        _rooms.value = response.data.filterNotNull()
                        _roomsState.value = RoomsState.Success
                    } else {
                        _roomsState.value = RoomsState.Error(response.message ?: "Gagal memuat ruangan")
                    }
                }
        }
    }

    fun getRoomDetail(id: String, day: String) {
        viewModelScope.launch {
            _roomDetailState.value = RoomDetailState.Loading
            roomRepository.getRoomDetail(id, day)
                .catch { exception ->
                    _roomDetailState.value = RoomDetailState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    if (response.status == "success" && response.data != null) {
                        _roomDetail.value = response.data
                        _roomDetailState.value = RoomDetailState.Success
                    } else {
                        _roomDetailState.value = RoomDetailState.Error(response.message ?: "Gagal memuat detail ruangan")
                    }
                }
        }
    }

    fun createBooking(request: CreateBookingRequest) {
        viewModelScope.launch {
            _bookingState.value = BookingState.Loading
            bookingRepository.createBooking(request)
                .catch { exception ->
                    _bookingState.value = BookingState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    if (response.status == "success") {
                        _bookingState.value = BookingState.Success(response.message ?: "Booking berhasil dibuat")
                    } else {
                        _bookingState.value = BookingState.Error(response.message ?: "Gagal membuat booking")
                    }
                }
        }
    }
}

