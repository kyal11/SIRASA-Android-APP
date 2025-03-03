package com.dev.sirasa.screens.user.history

import com.dev.sirasa.screens.user.room.RoomsState

sealed class HistoryBookingState{
    data object Idle : HistoryBookingState()
    data object Loading : HistoryBookingState()
    data object Success : HistoryBookingState()
    data class Error(val message: String) : HistoryBookingState()
}

sealed class HistoryActiveState {
    data object Idle : HistoryActiveState()
    data object Loading : HistoryActiveState()
    data object Success : HistoryActiveState()
    data class Error(val message: String) : HistoryActiveState()
}

sealed class CancelBookingState {
    data object Idle : CancelBookingState()
    data class Loading(val bookingId: String) : CancelBookingState()
    data object Success : CancelBookingState()
    data class Error(val message: String) : CancelBookingState()
}