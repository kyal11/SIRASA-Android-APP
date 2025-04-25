package com.dev.sirasa.screens.admin.data.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.dev.sirasa.data.remote.response.room.RoomModel
import com.dev.sirasa.screens.admin.data.DataViewModel
import com.dev.sirasa.screens.user.room.DeleteRoomState
import com.dev.sirasa.screens.user.room.EditRoomState
import com.dev.sirasa.screens.user.room.RoomViewModel
import com.dev.sirasa.screens.user.room.RoomsDetailState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRoomScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    userRole: String?,
    roomId: String,
    viewModel: RoomViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val roomState by viewModel.roomsDetail.collectAsStateWithLifecycle()
    val updateRoomState by viewModel.editRoomState.collectAsStateWithLifecycle()
    val deleteRoomState by viewModel.deleteRoomState.collectAsStateWithLifecycle()
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    // State untuk menyimpan data ruangan
    var name by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getRoomDetail(roomId)
    }
    LaunchedEffect(roomState) {
        if (roomState is RoomsDetailState.Success) {
            val room = (roomState as RoomsDetailState.Success).data
            name = room.name
            floor = room.floor.toString()
            capacity = room.capacity.toString()
            startTime = room.startTime
            endTime = room.endTime
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Data Ruangan") },
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
                InputField("Nama Ruangan", name, readOnly = userRole != "superadmin") { name = it }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    InputField(
                        label = "Lantai",
                        value = floor,
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        readOnly = userRole != "superadmin"
                    ) { floor = it }

                    InputField(
                        label = "Kapasitas Ruangan",
                        value = capacity,
                        modifier = Modifier.weight(1f).padding(start = 8.dp),
                        readOnly = userRole != "superadmin"
                    ) { capacity = it }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TimePickerField("Waktu Mulai", startTime, Modifier.weight(1f).padding(end = 8.dp), readOnly = userRole != "superadmin") {
                        startTime = it
                    }
                    TimePickerField("Waktu Selesai", endTime, Modifier.weight(1f).padding(start = 8.dp), readOnly = userRole != "superadmin") {
                        endTime = it
                    }
                }
                if (userRole == "superadmin") {
                    Button(
                        onClick = {
                            showUpdateDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (updateRoomState is EditRoomState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Simpan Perubahan")
                        }
                    }

                    Button(
                        onClick = {
                            showDeleteDialog = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        if (deleteRoomState is DeleteRoomState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Hapus Ruangan")
                        }
                    }
                }
            }
        }
    }
    // Dialog Konfirmasi Update
    if (showUpdateDialog) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog = false },
            containerColor = Color.White,
            title = { Text("Konfirmasi Perubahan") },
            text = {
                Text("Apakah Anda yakin ingin memperbarui ruangan? Mengubah waktu mulai dan berakhir akan mempengaruhi seluruh jadwal ruangan dan membatalkan booking yang tidak sesuai dengan jadwal baru."
                    ,textAlign = TextAlign.Justify)
            },
            confirmButton = {
                Button(onClick = {
                    val roomModel = RoomModel(
                        name = name,
                        floor = floor.toIntOrNull() ?: 0,
                        capacity = capacity.toIntOrNull() ?: 0,
                        startTime = startTime,
                        endTime = endTime
                    )
                    viewModel.updateRoom(roomId, roomModel)
                    showUpdateDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUpdateDialog = false }) {
                    Text("Cancel", color = Color.Red)
                }
            }
        )
    }

    // Dialog Konfirmasi Delete
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Color.White,
            title = { Text("Konfirmasi Penghapusan") },
            text = { Text("Apakah Anda yakin ingin menghapus ruangan ini?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteRoom(roomId)
                    showDeleteDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Color.Red)
                }
            }
        )
    }
    // Menampilkan notifikasi
    LaunchedEffect(updateRoomState, deleteRoomState) {
        when (updateRoomState) {
            is EditRoomState.Success -> {
                snackbarHostState.showSnackbar("Ruangan berhasil diperbarui!")
                navController.popBackStack()
            }
            is EditRoomState.Error -> {
                snackbarHostState.showSnackbar((updateRoomState as EditRoomState.Error).message)
            }
            else -> {}
        }

        when (deleteRoomState) {
            is DeleteRoomState.Success -> {
                snackbarHostState.showSnackbar("Ruangan berhasil dihapus!")
                navController.popBackStack()
            }
            is DeleteRoomState.Error -> {
                snackbarHostState.showSnackbar((deleteRoomState as DeleteRoomState.Error).message)
            }
            else -> {}
        }
    }
}
