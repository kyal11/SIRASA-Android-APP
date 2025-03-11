package com.dev.sirasa.screens.admin.qr_code_booking

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.remote.response.booking.DataBooking
import com.dev.sirasa.data.repository.BookingRepository
import com.dev.sirasa.utils.JsonUtils.extractMessageFromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _bookingValidationState = MutableStateFlow<ScanState>(ScanState.Idle)
    val bookingValidationState: StateFlow<ScanState> = _bookingValidationState

    fun validateBooking(bookingId: String) {
        viewModelScope.launch {
            _bookingValidationState.value = ScanState.Loading
            try {
                bookingRepository.validationBooking(bookingId)
                    .collect { response ->
                        if (response.status == "success" && response.data != null) {
                            _bookingValidationState.value = ScanState.Success(response.data)
                        } else {
                            _bookingValidationState.value = ScanState.Error(response.message ?: "Booking tidak valid")
                        }
                    }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = extractMessageFromJson(errorBody) ?: "Terjadi kesalahan server"
                Log.e("ScanViewModel", "HTTP Error: $errorMessage", e)
                _bookingValidationState.value = ScanState.Error(errorMessage)
            } catch (e: IOException) {
                Log.e("ScanViewModel", "Network Error", e)
                _bookingValidationState.value = ScanState.Error("Koneksi gagal. Periksa koneksi internet Anda.")
            } catch (e: Exception) {
                Log.e("ScanViewModel", "Unknown Error", e)
                _bookingValidationState.value = ScanState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    fun resetValidationState() {
        _bookingValidationState.value = ScanState.Idle
    }
}

sealed class ScanState {
    data object Idle : ScanState()
    data object Loading : ScanState()
    data class Success(val data: DataBooking) : ScanState()
    data class Error(val message: String?) : ScanState()
}
