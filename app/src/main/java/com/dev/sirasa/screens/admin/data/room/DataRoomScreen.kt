package com.dev.sirasa.screens.admin.data.room

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.dev.sirasa.R
import com.dev.sirasa.screens.admin.data.DataViewModel
import com.dev.sirasa.screens.admin.data.booking.ExportDialogBooking
import com.dev.sirasa.screens.user.home.UserViewModel
import com.dev.sirasa.ui.theme.Green300
import com.dev.sirasa.ui.theme.Green700
import com.dev.sirasa.ui.theme.Green900
import com.dev.sirasa.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataRoomScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: DataViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val rooms by userViewModel.rooms.collectAsState()
    LaunchedEffect(Unit) {
        userViewModel.getAllRooms()
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Room Data") },
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
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(
                        rooms.size,
                        key = { index -> rooms[index].id!! }
                    ) { index ->
                        val room = rooms[index]
                        CardRoomData(
                            idBooking = room.id!!,
                            roomName = room.name,
                            capacity = room.capacity.toString(),
                            floor = room.floor.toString(),
                            startTime = room.startTime!!,
                            endTime = room.endTime!!,
                            onClick = {
                                navController.navigate("edit_room/${room.id}")
                            }
                        )
                    }
                }

            }
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 12.dp, end = 12.dp),
                onClick = {navController.navigate("add_room")},
                containerColor = Color.White,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(32.dp),
                    tint = Green700
                )
            }
        }
    }
}

@Composable
fun CardRoomData(
    idBooking: String,
    roomName: String,
    capacity: String,
    floor: String,
    startTime: String,
    endTime: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.padding(6.dp).fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    roomName,
                    style = Typography.titleLarge,
                    color = Green900
                )

                Text(
                    "Lantai ${floor}",
                    style = Typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Kapasitas ${capacity} orang",
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Jadwal Ruangan :",
                style = Typography.bodyMedium
            )
            Text(
                "${startTime} - ${endTime}",
                style = Typography.bodyMedium
            )
        }
    }
}