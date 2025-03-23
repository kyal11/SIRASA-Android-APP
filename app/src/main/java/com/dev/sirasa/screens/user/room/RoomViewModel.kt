package com.dev.sirasa.screens.user.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.remote.response.room.DataRoomDetail
import com.dev.sirasa.data.remote.response.room.RoomModel
import com.dev.sirasa.data.repository.RoomRepository
import com.dev.sirasa.utils.JsonUtils.extractMessageFromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _roomSlots = MutableStateFlow<List<DataRoomDetail?>>(emptyList())
    val roomSlots: StateFlow<List<DataRoomDetail?>> = _roomSlots.asStateFlow()

    private val _roomsSlotsState = MutableStateFlow<RoomsSlotsState>(RoomsSlotsState.Idle)
    val roomsSlotsState: StateFlow<RoomsSlotsState> = _roomsSlotsState.asStateFlow()

    private val _roomsDetailState = MutableStateFlow<RoomsDetailState>(RoomsDetailState.Idle)
    val roomsDetail: StateFlow<RoomsDetailState> = _roomsDetailState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _addRoomState = MutableStateFlow<AddRoomState>(AddRoomState.Idle)
    val addRoomState: StateFlow<AddRoomState> = _addRoomState.asStateFlow()

    private val _editRoomState = MutableStateFlow<EditRoomState>(EditRoomState.Idle)
    val editRoomState: StateFlow<EditRoomState> = _editRoomState.asStateFlow()

    private val _deleteRoomState = MutableStateFlow<DeleteRoomState>(DeleteRoomState.Idle)
    val deleteRoomState: StateFlow<DeleteRoomState> = _deleteRoomState.asStateFlow()

    fun refreshRooms(dayValue: String) {
        viewModelScope.launch {
            _isRefreshing.value = true
            getAllRoomsSlots(dayValue)
            delay(300)
            _isRefreshing.value = false
        }
    }

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

    fun getRoomDetail(id: String, day: String? = null) {
        viewModelScope.launch {
            _roomsDetailState.value = RoomsDetailState.Loading
            roomRepository.getRoomDetail(id, day)
                .catch { exception ->
                    _roomsDetailState.value = RoomsDetailState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    if (response.status == "success" && response.data != null) {
                        _roomsDetailState.value = RoomsDetailState.Success(response.data)
                    } else {
                        _roomsDetailState.value = RoomsDetailState.Error(response.message ?: "Gagal memuat ruangan")
                    }
                }
        }
    }

    fun createRoom(roomModel: RoomModel) {
        viewModelScope.launch {
            _addRoomState.value = AddRoomState.Loading
            try {
                roomRepository.createRoom(roomModel).collect { response ->
                    if (response.status == "success") {
                        _addRoomState.value = AddRoomState.Success
                    } else {
                        _addRoomState.value =
                            AddRoomState.Error(response.message ?: "Gagal membuat ruangan")
                    }
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = extractMessageFromJson(errorBody) ?: "Terjadi kesalahan jaringan"
                _addRoomState.value = AddRoomState.Error(errorMessage)
            } catch (e: IOException) {
                _addRoomState.value = AddRoomState.Error("Koneksi gagal. Periksa koneksi internet Anda.")
            } catch (e: Exception) {
                _addRoomState.value = AddRoomState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    fun updateRoom(id: String, roomModel: RoomModel) {
        viewModelScope.launch {
            _editRoomState.value = EditRoomState.Loading
            try {
                roomRepository.updateRoom(id, roomModel).collect{ response ->
                    if (response.status == "success") {
                        _editRoomState.value = EditRoomState.Success
                    } else {
                        _editRoomState.value = EditRoomState.Error(response.message ?: "Gagal memperbarui ruangan")
                    }
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = extractMessageFromJson(errorBody) ?: "Terjadi kesalahan jaringan"
                _editRoomState.value = EditRoomState.Error(errorMessage)
            } catch (e: IOException) {
                _editRoomState.value = EditRoomState.Error("Koneksi gagal. Periksa koneksi internet Anda.")
            } catch (e: Exception) {
                _editRoomState.value = EditRoomState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }
    fun deleteRoom(id: String) {
        viewModelScope.launch {
            _deleteRoomState.value = DeleteRoomState.Loading
            roomRepository.deletedRoom(id)
                .catch { exception ->
                    _deleteRoomState.value = DeleteRoomState.Error(exception.message ?: "Terjadi kesalahan")
                }
                .collect { response ->
                    if (response.status == "success") {
                        _deleteRoomState.value = DeleteRoomState.Success
                    } else {
                        _deleteRoomState.value = DeleteRoomState.Error(response.message ?: "Gagal menghapus ruangan")
                    }
                }
        }
    }
}
