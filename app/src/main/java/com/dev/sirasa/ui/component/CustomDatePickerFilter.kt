package com.dev.sirasa.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateFieldFilter(
    onDateSelected: (String) -> Unit,
    placeHolder: String,
    modifier: Modifier = Modifier
) {
    var selectedDateFilter by remember { mutableStateOf<String?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDateFilter ?: "",
        onValueChange = {},
        placeholder = { Text(placeHolder, color = Color.Gray, style = MaterialTheme.typography.bodyMedium) },
        textStyle = MaterialTheme.typography.bodyMedium,
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE2E8F0),
        ),
        modifier = modifier
            .background(Color.White, RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModalFilter(
            onDateSelected = { dateMillis ->
                dateMillis?.let {
                    selectedDateFilter = convertMillisToDate(it)
                    onDateSelected(convertMillisToApiFormat(it))
                }
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

fun convertMillisToApiFormat(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalFilter(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val customColors = DatePickerDefaults.colors(
        containerColor = Color.White,
        selectedDayContainerColor = Color(0xFF00AA00),
        selectedDayContentColor = Color.White,
        todayContentColor = Color(0xFF00AA00),
        todayDateBorderColor = Color(0xFF00AA00),
        titleContentColor = Color.Black,
        headlineContentColor = Color.Black,
        weekdayContentColor = Color(0xFF505050),
        selectedYearContainerColor = Color(0xFF00AA00),
        selectedYearContentColor = Color.White
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { onDateSelected(it) }
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
        colors = DatePickerDefaults.colors(
            containerColor = Color.White
        ),
    ) {
        DatePicker(state = datePickerState, colors = customColors)
    }
}
