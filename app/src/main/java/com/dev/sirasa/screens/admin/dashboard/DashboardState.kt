package com.dev.sirasa.screens.admin.dashboard

import com.dev.sirasa.data.remote.response.booking.BookingSummaryResponse

sealed class DashboardState {
    data object Loading : DashboardState()
    data class Success(val data: BookingSummaryResponse) : DashboardState()
    data class Error(val message: String) : DashboardState()
}