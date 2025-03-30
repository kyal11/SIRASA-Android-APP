package com.dev.sirasa.screens.common.faq

import com.dev.sirasa.data.remote.response.faq.DataFaq

sealed class FaqState {
    data object Idle : FaqState()
    data object Loading : FaqState()
    data class Success (val data: List<DataFaq>) : FaqState()
    data class Error(val message: String) : FaqState()
}