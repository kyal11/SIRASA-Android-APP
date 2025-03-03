package com.dev.sirasa.screens.user.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.sirasa.data.remote.response.booking.BookingSlotItem
import com.dev.sirasa.data.remote.response.booking.SlotBooking
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.theme.Green300
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography
import com.dev.sirasa.utils.generateQrCodeBitmap
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun UserHistoryScreen(navController: NavController, viewModel: HistoryMainModel = hiltViewModel()) {
    val history by viewModel.history.collectAsState()
    val historyActive by viewModel.historyActive.collectAsState()
    val historyState by viewModel.historyState.collectAsState()
    val activeState by viewModel.activeState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getHistoryUser()
        viewModel.getActiveBooking()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        if (activeState is HistoryActiveState.Success && historyActive.isNotEmpty()) {
            Box(
                modifier = Modifier.weight(2f)
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Peminjaman yang sedang berlangsung",
                            style = Typography.titleMedium
                        )
                        IconButton(onClick = {
                            navController.navigate("more_booking")
                        }, modifier = Modifier.size(24.dp)) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "More",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    when (activeState) {
                        is HistoryActiveState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingCircular(true, modifier = Modifier.align(Alignment.Center))
                            }
                        }

                        is HistoryActiveState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (activeState as HistoryActiveState.Error).message,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        is HistoryActiveState.Success -> {
                            if (historyActive.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Tidak ada peminjaman yang sedang berlangsung")
                                }
                            } else {
                                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                    items(historyActive) { booking ->
                                        val roomName =
                                            booking.room?.name ?: "Ruangan tidak tersedia"
                                        val date =
                                            formatDate(booking.bookingSlot?.firstOrNull()?.slot?.date)
                                        val timeSlot = formatTimeSlot(booking.bookingSlot)

                                        CardHistory(
                                            idBooking = booking.id,
                                            dateBooking = date,
                                            name = roomName,
                                            phone = "tes",
                                            timeSlot = timeSlot,
                                            location = "Lantai ${booking.room?.floor}",
                                            participants = "${booking.participant} Orang",
                                            description = if (booking.description.isNullOrEmpty()) "tidak ada" else booking.description,
                                            status = booking.status,
                                            onCancelBooking = {
                                                viewModel.cancelBooking(booking.id)
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                            }
                        }

                        else -> { /* Idle state */
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Bagian Riwayat Peminjaman
        if (historyState is HistoryBookingState.Success && history.isNotEmpty()) {
        Column(modifier = Modifier.weight(3f)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Riwayat Peminjaman",
                    style = Typography.titleMedium
                )
                IconButton(onClick = {
                    navController.navigate("more_history")
                }, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "More",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            when (historyState) {
                is HistoryBookingState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingCircular(true, modifier = Modifier.align(Alignment.Center))
                    }
                }

                is HistoryBookingState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (historyState as HistoryBookingState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is HistoryBookingState.Success -> {
                    if (history.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada peminjaman yang sedang berlangsung")
                        }
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(history) { booking ->
                                val roomName = booking.room?.name ?: "Ruangan tidak tersedia"
                                val date =
                                    formatDate(booking.bookingSlot?.firstOrNull()?.slot?.date)
                                val timeSlot = formatTimeSlot(booking.bookingSlot)

                                CardHistory(
                                    idBooking = booking.id,
                                    dateBooking = date,
                                    name = roomName,
                                    phone = "tes",
                                    timeSlot = timeSlot,
                                    location = "Lantai ${booking.room?.floor}",
                                    participants = "${booking.participant} Orang",
                                    description = if (booking.description.isNullOrEmpty()) "tidak ada" else booking.description,
                                    status = booking.status,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                else -> { /* Idle state */
                }
            }
        }
        }
    }
}
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

// Extension function to use with LazyColumn
fun <T> LazyListScope.items(
    items: List<T>,
    itemContent: @Composable (T) -> Unit
) {
    items(items.size) {
        itemContent(items[it])
    }
}
@Composable
fun CardHistory(
    idBooking: String,
    dateBooking: String,
    name: String,
    phone: String,
    timeSlot: String,
    location: String,
    participants: String,
    description: String,
    status: String,
    onCancelBooking: (() -> Unit)? = null
) {
    val (badgeColor, statusText) = when (status) {
        "cancel" -> Color.Red to "Dibatalkan"
        "booked" -> Color.Yellow to "Dipesan"
        "done" -> Green300 to "Selesai"
        else -> Color.Gray to "Unknown"
    }
    var showQr by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(dateBooking, color = MaterialTheme.colorScheme.onPrimaryContainer)
                if (status == "booked") {
                    Box {
                        IconButton(onClick = { menuExpanded = true }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier.background(Color.White),
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    menuExpanded = false
                                    if (onCancelBooking != null) {
                                        onCancelBooking()
                                    }
                                },
                                text = { Text("Cancel Booking") }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, maxLines = 2 , color = MaterialTheme.colorScheme.onPrimaryContainer, style = Typography.titleLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(phone, style = Typography.bodyMedium)
                }
                Badge(
                    modifier = Modifier.padding(8.dp),
                    containerColor = badgeColor,
                    contentColor = if (status == "cancel") Color.White else Color.Black
                ) {
                    Text(statusText, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = Typography.titleMedium)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(timeSlot, style = Typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(location, style = Typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(participants, style = Typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Text(description, style = Typography.bodyMedium, maxLines = 2)

            if (status == "booked") {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { showQr = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally).height(42.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding =  PaddingValues(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black)
                ) {
                    Text("Show QR Code", style = Typography.bodyMedium)
                }
            }
        }
    }
    if (showQr) {
        QrCodeDialog(idBooking) { showQr = false }
    }
}
@Composable
fun QrCodeDialog(idBooking: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = Color.White,
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Tutup")
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth() ,horizontalAlignment = Alignment.CenterHorizontally) {
                Text("QR Code Booking", style = Typography.titleMedium)
                Spacer(modifier = Modifier.height(10.dp))
                GenerateQrCode(idBooking)
            }
        }
    )
}
@Composable
fun GenerateQrCode(text: String) {
    val qrCodeBitmap = remember(text) { generateQrCodeBitmap(text) }
    qrCodeBitmap?.let { bitmap ->
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "QR Code",
            modifier = Modifier.size(250.dp)
        )
    }
}
//@Preview(showBackground = true)
//@Composable
//fun PreviewCardHistory() {
//    SirasaTheme {
//        CardHistory(
//            idBooking = "12345",
//            dateBooking = "10 Maret 2025",
//            name = "Rizky Al Arief",
//            phone = "083812721450",
//            timeSlot = "10:00 - 12:00",
//            location = "Ruang Diskusi Merpati",
//            participants = "10 Orang",
//            description = "Sesi Brainstorming untuk pengembangan strategi pemasaran produk baru.",
//            status = "Booking",
//            onCancelBooking = {}
//        )
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun PreviewHistory() {
//    SirasaTheme {
//        UserHistoryScreen()
//    }
//}
//@Preview(showBackground = true, name = "Phone Preview", device = Devices.PHONE)
//@Composable
//fun PreviewPhone() {
//    SirasaTheme {
//        UserHistoryScreen()
//    }
//}
//
//@Preview(showBackground = true, name = "Tablet Preview", device = Devices.TABLET)
//@Composable
//fun PreviewTablet() {
//    SirasaTheme {
//        UserHistoryScreen()
//    }
//}
//
//@Preview(showBackground = true, name = "Small Device Preview", widthDp = 320, heightDp = 640)
//@Composable
//fun PreviewSmallDevice() {
//    SirasaTheme {
//        UserHistoryScreen()
//    }
//}
//
//@Preview(showBackground = true, name = "Large Device Preview", widthDp = 600, heightDp = 800)
//@Composable
//fun PreviewLargeDevice() {
//    SirasaTheme {
//        UserHistoryScreen()
//    }
//}