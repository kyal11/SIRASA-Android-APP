package com.dev.sirasa.screens.user.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.remote.response.room.DataRoomDetail
import com.dev.sirasa.data.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel(){
    private val _roomSlots = MutableStateFlow<List<DataRoomDetail?>>(emptyList())
    val roomSlots: StateFlow<List<DataRoomDetail?>> = _roomSlots.asStateFlow()

    private val _roomsSlotsState = MutableStateFlow<RoomsSlotsState>(RoomsSlotsState.Idle)
    val roomsSlotsState: StateFlow<RoomsSlotsState> = _roomsSlotsState.asStateFlow()


    fun getAllRoomsSlots(day: String) {
        viewModelScope.launch {
            _roomsSlotsState.value = RoomsSlotsState.Loading
            roomRepository.getRoomWithSlots(day)
                .catch { exception ->
                    _roomsSlotsState.value = RoomsSlotsState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    if (response.status == "success" && response.data != null) {
                        _roomSlots.value = response.data.filterNotNull()
                        _roomsSlotsState.value = RoomsSlotsState.Success
                    } else {
                        _roomsSlotsState.value = RoomsSlotsState.Error(response.message ?: "Gagal memuat ruangan")
                    }
                }
        }
    }
}