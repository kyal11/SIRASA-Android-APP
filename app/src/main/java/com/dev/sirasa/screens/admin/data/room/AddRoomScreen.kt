package com.dev.sirasa.screens.admin.data.room

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.sirasa.screens.admin.data.DataViewModel
import com.dev.sirasa.ui.theme.Typography
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.sirasa.data.remote.response.room.RoomModel
import com.dev.sirasa.screens.user.room.AddRoomState
import com.dev.sirasa.screens.user.room.RoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoomScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    userRole: String?,
    viewModel: RoomViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    val addRoomState by viewModel.addRoomState.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Room Data") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InputField("Nama Ruangan", name) { name = it }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    InputField(
                        label = "Lantai",
                        value = floor,
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) { floor = it }

                    InputField(
                        label = "Kapasitas Ruangan",
                        value = capacity,
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    ) { capacity = it }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TimePickerField("Start Time", startTime, Modifier.weight(1f).padding(end = 8.dp)) {
                        startTime = it
                    }
                    TimePickerField("End Time", endTime, Modifier.weight(1f).padding(start = 8.dp)) {
                        endTime = it
                    }
                }

                Button(
                    onClick = {
                        val roomModel = RoomModel(
                            name = name,
                            floor = floor.toIntOrNull() ?: 0,
                            capacity = capacity.toIntOrNull() ?: 0,
                            startTime = startTime,
                            endTime = endTime
                        )
                        viewModel.createRoom(roomModel)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = name.isNotBlank() && floor.isNotBlank() && capacity.isNotBlank()
                ) {
                    if (addRoomState is AddRoomState.Loading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("Buat Ruangan")
                    }
                }
            }
        }
    }

    // Menampilkan notifikasi
    LaunchedEffect(addRoomState) {
        when (addRoomState) {
            is AddRoomState.Success -> {
                snackbarHostState.showSnackbar("Ruangan berhasil dibuat!")
                navController.popBackStack()
            }
            is AddRoomState.Error -> {
                snackbarHostState.showSnackbar((addRoomState as AddRoomState.Error).message)
            }
            else -> {}
        }
    }
}


@Composable
fun InputField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean? = false,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(text = label, style = Typography.bodyMedium, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = { Text("Masukkan $label", style = Typography.bodyMedium, color = Color.Gray) },
            keyboardOptions = KeyboardOptions.Default,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color(0xFFE2E8F0)),
            singleLine = true,
            readOnly = readOnly!!
        )
    }
}

@Composable
fun TimePickerField(label: String, time: String, modifier: Modifier, readOnly: Boolean = false, onTimeSelected: (String) -> Unit) {
    var showTimePicker by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(text = label, style = Typography.bodyMedium, modifier = Modifier.padding(bottom = 8.dp))

        OutlinedTextField(
            value = time ?: "",
            onValueChange = {},
            textStyle = MaterialTheme.typography.bodyMedium,
            readOnly = true,
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color(0xFFE2E8F0)),
            placeholder = {
                Text(
                    text = "HH:MM",
                    style = Typography.bodyMedium,
                    color = Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .pointerInput(Unit) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            showTimePicker = true
                        }
                    }
                }
        )
    }

    if (showTimePicker && !readOnly) {
        TimePickerDialogM3(
            initialHour = time.take(2).toIntOrNull() ?: 0,
            onTimeSelected = {
                onTimeSelected(it)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogM3(
    initialHour: Int,
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = 0,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Waktu") },
        containerColor = Color.White,
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TimeInput(state = timePickerState)
            }
        },
        confirmButton = {
            Button(onClick = {
                val selectedTime = String.format("%02d:00", timePickerState.hour)
                onTimeSelected(selectedTime)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Red)
            }
        }
    )
}

