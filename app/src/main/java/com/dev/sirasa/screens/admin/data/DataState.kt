package com.dev.sirasa.screens.admin.data

import com.dev.sirasa.data.remote.response.booking.DataBooking
import com.dev.sirasa.data.remote.response.booking.DataBookingsPaginate
import com.dev.sirasa.data.remote.response.crm.DataCrm
import com.dev.sirasa.data.remote.response.room.DataRoomDetail
import com.dev.sirasa.data.remote.response.user.DataUser
import com.dev.sirasa.data.remote.response.user.GetDetailUserResponse


sealed class DataSummaryState {
    data object Idle : DataSummaryState()
    data object Loading : DataSummaryState()
    data class Success (val data: DataCrm) : DataSummaryState()
    data class Error(val message: String) : DataSummaryState()
}

sealed class DataAllUsersState {
    data object Idle : DataAllUsersState()
    data object Loading : DataAllUsersState()
    data class Success(val data: List<DataUser>) : DataAllUsersState()
    data class Error(val message: String) : DataAllUsersState()
}
sealed class DataAddUserState {
    data object Idle : DataAddUserState()
    data object Loading : DataAddUserState()
    data class Success(val data: GetDetailUserResponse) : DataAddUserState()
    data class Error(val message: String) : DataAddUserState()
}
sealed class DataUserDetailState {
    data object Idle : DataUserDetailState()
    data object Loading : DataUserDetailState()
    data class Success(val data: DataUser) : DataUserDetailState()
    data class Error(val message: String) : DataUserDetailState()
}

sealed class DataUserDeleteState {
    data object Idle : DataUserDeleteState()
    data object Loading : DataUserDeleteState()
    data class Success(val data: String) : DataUserDeleteState()
    data class Error(val message: String) : DataUserDeleteState()
}
sealed class UpdateUserState {
    data object Idle : UpdateUserState()
    data object Loading : UpdateUserState()
    data object Success : UpdateUserState()
    data class Error(val message: String) : UpdateUserState()
}

sealed class DataBookingsState {
    data object Idle : DataBookingsState()
    data object Loading : DataBookingsState()
    data class Success(val data: List<DataBookingsPaginate>) : DataBookingsState()
    data class Error(val message: String) : DataBookingsState()
}

sealed class DoneBookingState {
    data object Idle : DoneBookingState()
    data object Loading : DoneBookingState()
    data object Success : DoneBookingState()
    data class Error(val message: String) : DoneBookingState()
}

sealed class ExportState {
    data object Idle : ExportState()
    data object Loading : ExportState()
    data object Success : ExportState()
    data class Error(val message: String) : ExportState()
}
