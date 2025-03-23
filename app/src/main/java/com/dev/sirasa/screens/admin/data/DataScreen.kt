package com.dev.sirasa.screens.admin.data

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun DataScreen(navController: NavController, viewModel: DataViewModel = hiltViewModel()) {
    val summaryState by viewModel.summaryData.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getSummary()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Data SIRASA",
            style = Typography.displayLarge
        )
        Text(
            "Content Management System SIRASA",
            style = Typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (summaryState) {
            is DataSummaryState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingCircular(
                        true,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            is DataSummaryState.Error -> {
                Text(
                    text = (summaryState as DataSummaryState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = Typography.bodyLarge
                )
            }
            is DataSummaryState.Success -> {
                val data = (summaryState as DataSummaryState.Success).data
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Data User Card
                    CardDataUser(
                        title = "Data Pengguna",
                        total = data.user?.total.toString(),
                        userData = listOf(
                            "Super Admin" to data.user?.totalRoleSuperadmin.toString(),
                            "Admin" to data.user?.totalRoleAdmin.toString(),
                            "Pengguna" to data.user?.totalRoleUser.toString()
                        ),
                        onClick = { navController.navigate("data_users") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Data Room Card
                    data.rooms?.dataRooms?.let {
                        CardDataRoom(
                            title = "Data Ruangan",
                            total = data.rooms?.totalRooms.toString(),
                            roomData = it.map { room ->
                                Triple(
                                    room?.name,
                                    room?.capacity.toString(),
                                    "${room?.startTime}-${room?.endTime}"
                                )
                            },
                            onClick = { navController.navigate("data_rooms") }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Data Booking Card
                    CardDataBooking(
                        title = "Data Booking",
                        total = data.booking?.totalBooking.toString(),
                        bookingData = listOf(
                            "Selesai" to data.booking?.totalDone.toString(),
                            "Aktif" to data.booking?.totalBooked.toString(),
                            "Dibatalkan" to data.booking?.totalCancel.toString()
                        ),
                        onClick = { navController.navigate("data_bookings") }
                    )
                }
            }
            else -> {
                // Idle state or empty screen
                Text(
                    text = "Loading data...",
                    style = Typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun CardDataBooking(title: String, total: String, bookingData: List<Pair<String, String>>, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $total",
                style = Typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            bookingData.forEachIndexed { index, data ->
                BookingDataRow(status = data.first, count = data.second)
                if (index < bookingData.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun BookingDataRow(status: String, count: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = status, style = Typography.bodyMedium)
        Text(text = count, style = Typography.bodyMedium)
    }
}


@Composable
fun CardDataUser(title: String, total: String, userData: List<Pair<String, String>>, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $total",
                style = Typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            userData.forEachIndexed { index, data ->
                UserDataRow(role = data.first, count = data.second)
                if (index < userData.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun UserDataRow(role: String, count: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = role, style = Typography.bodyMedium)
        Text(text = count, style = Typography.bodyMedium)
    }
}

@Composable
fun CardDataRoom(title: String, total: String, roomData: List<Triple<String?, String, String>>, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $total",
                style = Typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Name", style = Typography.labelLarge, modifier = Modifier.weight(2f))
                Text(text = "Capacity", style = Typography.labelLarge, modifier = Modifier.weight(1f))
                Text(text = "Time", style = Typography.labelLarge, modifier = Modifier.weight(2f), textAlign = TextAlign.End)
            }
            Divider()

            roomData.forEachIndexed { index, data ->
                data.first?.let { RoomDataRow(name = it, capacity = data.second, time = data.third) }
                if (index < roomData.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun RoomDataRow(name: String, capacity: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(text = name, style = Typography.bodyMedium, modifier = Modifier.weight(2f))
        Text(text = capacity, style = Typography.bodyMedium, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = time, style = Typography.bodyMedium, modifier = Modifier.weight(2f), textAlign = TextAlign.End)
    }
}
//@Preview(showBackground = true)
//@Composable
//fun PreviewCardScreen() {
//    SirasaTheme {
//        CardDataUser(
//            title = "Data Pengguna",
//            total = "100",
//            userData = listOf(
//                "Super Admin" to "10",
//                "Admin" to "10",
//                "Pengguna" to "10"
//            )
//        )
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun PreviewDataScreen() {
//    SirasaTheme {
//        DataScreen()
//    }
//}