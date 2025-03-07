package com.dev.sirasa.utils

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import com.dev.sirasa.data.remote.response.booking.BookingSlotItem
import com.dev.sirasa.data.remote.response.booking.SlotsRecommendation
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


fun formatDate(dateString: String?): String {
    if (dateString == null) return "Tanggal tidak tersedia"

    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)

        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
        outputFormat.format(date!!)
    } catch (e: Exception) {
        "Tanggal tidak valid"
    }
}

// Helper function to format time slot
fun formatTimeSlot(slots: List<BookingSlotItem?>?): String {
    if (slots.isNullOrEmpty()) return "Waktu tidak tersedia"

    val sortedSlots = slots.sortedBy { it?.slot?.startTime }

    val startTime = sortedSlots.firstOrNull()?.slot?.startTime
    val endTime = sortedSlots.lastOrNull()?.slot?.endTime

    return if (startTime != null && endTime != null) {
        "$startTime - $endTime"
    } else {
        "Waktu tidak tersedia"
    }
}

fun formatTimeSlotRecommendation(slots: List<SlotsRecommendation?>?): String {
    if (slots.isNullOrEmpty()) return "Waktu tidak tersedia"

    val sortedSlots = slots.sortedBy { it?.startTime }

    val startTime = sortedSlots.firstOrNull()?.startTime
    val endTime = sortedSlots.lastOrNull()?.endTime

    return if (startTime != null && endTime != null) {
        "$startTime - $endTime"
    } else {
        "Waktu tidak tersedia"
    }
}