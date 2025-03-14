package com.dev.sirasa.screens.admin.data

import com.dev.sirasa.data.remote.response.crm.DataCrm
import com.dev.sirasa.data.remote.response.user.DataUser


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
