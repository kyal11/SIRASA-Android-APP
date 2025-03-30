package com.dev.sirasa.screens.common.faq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.sirasa.data.remote.response.faq.DataFaq
import com.dev.sirasa.data.repository.FaqRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(
    private val faqRepository: FaqRepository
) : ViewModel() {

    private val _faqState = MutableStateFlow<FaqState>(FaqState.Idle)
    val faqState: StateFlow<FaqState> = _faqState

    fun getAllFaqs() {
        viewModelScope.launch {
            _faqState.value = FaqState.Loading
            try {
                faqRepository.getAllFaq()
                    .collectLatest { response ->
                        if (response.status == "success") {
                            _faqState.value = FaqState.Success((response.data ?: emptyList()) as List<DataFaq>)
                        } else {
                            _faqState.value = FaqState.Error(response.message ?: "Failed to load FAQs")
                        }
                    }
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "Terjadi kesalahan server"
                _faqState.value = FaqState.Error(errorMessage)
            } catch (e: IOException) {
                _faqState.value = FaqState.Error("Koneksi gagal. Periksa koneksi internet Anda.")
            } catch (e: Exception) {
                _faqState.value = FaqState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }
}
