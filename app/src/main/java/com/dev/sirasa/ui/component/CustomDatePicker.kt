package com.dev.sirasa.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DateField(
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tanggal Peminjaman",
            style = Typography.displayMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = selectedDate?.let { convertMillisToDate(it) } ?: "",
            onValueChange = {},
            placeholder = { Text("DD/MM/YYYY", color = Color.Gray, style = Typography.bodyMedium) },
            textStyle = MaterialTheme.typography.bodyMedium,
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Select Date")
            },

            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE2E8F0),
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .pointerInput(selectedDate) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showModal = true
                        }
                    }
                }
        )
    }
    if (showModal) {
        DatePickerModal(
            onDateSelected = {
                selectedDate = it
            },
            onDismiss = {
                showModal = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val todayUtc = getUtcMillisForDayOffset(0) // Hari ini (00:00 UTC)
    val tomorrowUtc = getUtcMillisForDayOffset(1) // Besok (00:00 UTC)
    val dayAfterTomorrowUtc = getUtcMillisForDayOffset(2) // Lusa (00:00 UTC)

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis in todayUtc .. dayAfterTomorrowUtc
            }
        }
    )
    val customColors = DatePickerDefaults.colors(
        // Warna container keseluruhan
        containerColor = Color(0xFFF0F8FF),
        // Warna untuk hari yang dipilih
        selectedDayContainerColor = Color(0xFF00AA00),  // Hijau untuk hari yang dipilih
        selectedDayContentColor = Color.White,
        // Warna untuk hari ini
        todayContentColor = Color(0xFF0000FF),  // Biru untuk hari ini
        todayDateBorderColor = Color(0xFF0000FF),
        // Warna untuk header dan konten lainnya
        titleContentColor = Color(0xFF000000),
        headlineContentColor = Color(0xFF000000),
        weekdayContentColor = Color(0xFF505050),
        // Warna untuk tahun yang dipilih
        selectedYearContainerColor = Color(0xFF00AA00),
        selectedYearContentColor = Color.White
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        colors = customColors,
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
fun getUtcMillisForDayOffset(offset: Int): Long {
    val calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    calendar.set(java.util.Calendar.MILLISECOND, 0)
    calendar.add(java.util.Calendar.DAY_OF_MONTH, offset) // Geser hari
    return calendar.timeInMillis
}
@Preview(showBackground = true)
@Composable
fun PreviewDateField() {
    SirasaTheme {
        DateField()
    }
}
