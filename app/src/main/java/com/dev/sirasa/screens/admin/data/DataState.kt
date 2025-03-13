package com.dev.sirasa.screens.admin.data

import com.dev.sirasa.data.remote.response.crm.DataCrm


sealed class DataSummaryState {
    data object Idle : DataSummaryState()
    data object Loading : DataSummaryState()
    data class Success (val data: DataCrm) : DataSummaryState()
    data class Error(val message: String) : DataSummaryState()
}