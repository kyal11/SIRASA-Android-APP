package com.dev.sirasa.screens.user.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.sirasa.R
import com.dev.sirasa.data.remote.response.booking.CreateBookingRequest
import com.dev.sirasa.data.remote.response.room.SlotsDetailItem
import com.dev.sirasa.screens.common.login.LoginState
import com.dev.sirasa.screens.common.profile.ProfileState
import com.dev.sirasa.ui.component.CustomOptionTime
import com.dev.sirasa.ui.component.DateField
import com.dev.sirasa.ui.component.DropdownField
import com.dev.sirasa.ui.component.InputField
import com.dev.sirasa.ui.component.InputFieldTextArea
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.theme.Green600
import com.dev.sirasa.ui.theme.Green800
import com.dev.sirasa.ui.theme.Green900
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun UserHomeScreen(snackbarHostState: SnackbarHostState, userViewModel: UserViewModel = hiltViewModel()) {
    var description by remember { mutableStateOf("") }
    var selectedRoomId by remember { mutableStateOf<String?>(null) }
    var selectedRoomName by remember { mutableStateOf("") }
    var selectedSlots by remember { mutableStateOf<List<String>>(emptyList()) }
    var capacity by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    val bookingState by userViewModel.bookingState.collectAsState()


    //Data View Model
    val rooms by userViewModel.rooms.collectAsState()
    LaunchedEffect(Unit) {
        userViewModel.getAllRooms()
    }
    val roomDetail by userViewModel.roomDetail.collectAsState()

    LaunchedEffect(selectedRoomId, selectedDate) {
        if (selectedRoomId != null && selectedDate != null) {
            userViewModel.getRoomDetail(selectedRoomId!!, selectedDate!!)
        }
    }

    LaunchedEffect(selectedRoomId) {
        selectedSlots = emptyList()
    }

    LaunchedEffect(bookingState) {
        when (bookingState) {
            is BookingState.Success -> {
                snackbarHostState.showSnackbar("Logout berhasil!")
            }
            is BookingState.Error -> {
                val errorMessage = (bookingState as BookingState.Error).message
                snackbarHostState.showSnackbar(message = errorMessage, actionLabel = "OK")
            }
            else -> {}
        }
    }
    val availableSlots = roomDetail?.slots?.map { slot ->
        Pair("${slot?.startTime}-${slot?.endTime}", slot?.id)
    } ?: emptyList()


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp, horizontal = 16.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.perpus_logo),
                contentDescription = "Logo Sirasa",
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Selamat Datang di SIRASA",
                style = Typography.displayLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Silakan isi formulir di bawah ini untuk meminjam ruang diskusi kami.",
                style = Typography.displayMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            DateField { date -> selectedDate = date }
            Spacer(modifier = Modifier.height(8.dp))

            DropdownField(
                options = rooms.map { it.name },
                selectedOption = selectedRoomName,
                onOptionSelected = { selectedName ->
                    selectedRoomName = selectedName
                    selectedRoomId = rooms.find { it.name == selectedName }?.id
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CustomOptionTime(
                    if (selectedRoomName.isEmpty() && selectedDate == null) true else false,
                    options = availableSlots,
                    selectedOptions = selectedSlots.mapNotNull { id ->
                        availableSlots.find { it.second == id }?.first
                    },
                    onOptionSelected = { selectedText ->
                        selectedSlots = selectedText
                    },
                    modifier = Modifier.weight(1f)
                )

                InputField(
                    label = "Jumlah Peserta",
                    placeHolder = "Peserta",
                    value = capacity,
                    onValueChange = { capacity = it },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.width(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            InputFieldTextArea(
                "Keterangan Keperluan",
                description,
                { description = it },
                KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Red)) { append("* ") }
                    append("Harap diperhatikan: Jika Anda tidak tiba di resepsionis dalam 10 menit setelah waktu mulai yang Anda pilih, reservasi akan otomatis dibatalkan.")
                },
                style = Typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val request = CreateBookingRequest(
                        roomId = selectedRoomId!!,
                        bookingSlotId = selectedSlots,
                        participant = capacity.toInt(),
                        description = description
                    )
                    userViewModel.createBooking(request)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),

                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Submit", style = Typography.titleMedium)
            }
        }
        if (bookingState is BookingState.Loading) {
            LoadingCircular(true, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun DialogBooking(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = Green900
                        )
                    }
                }
                // Jika Berhasil
//                Column (
//                    modifier = Modifier.padding(8.dp)
//                ) {
//                    Text(
//                        "Hasil Pemesanan",
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.titleLarge,
//                        color = Green900
//                    )
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        "5 Maret 2025",
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.End,
//                        style = MaterialTheme.typography.displayMedium,
//                        color = Green800
//                    )
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        "Rizky Al Arief",
//                        style = MaterialTheme.typography.titleMedium,
//                        color = Green900
//                    )
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        "0838127215",
//                        style = MaterialTheme.typography.displayMedium,
//                    )
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        "12:00 - 14:00",
//                        style = MaterialTheme.typography.displayMedium,
//                    )
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        "Ruang Danantara",
//                        style = MaterialTheme.typography.displayMedium,
//                    )
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        "10 Oranng",
//                        style = MaterialTheme.typography.displayMedium,
//                    )
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        "Deskripsi Deskripsi Deskripsi Deskripsi",
//                        style = MaterialTheme.typography.displayMedium,
//                    )
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        buildAnnotatedString {
//                            withStyle(style = SpanStyle(color = Color.Red)) { append("* ") }
//                            append("Temukan semua detail pemesanan ruangan Anda di menu Riwayat. Jadi, jika Anda perlu memeriksa pemesanan sebelumnya, cukup buka menu Riwayat. Terima kasih!")
//                        },
//                        style = MaterialTheme.typography.titleMedium,
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center
//                    )
//                }

                // Jika gagal dan rekomendasi
                Column (
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        "Hasil Pemesanan",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        color = Green900
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tutup",
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                    Text(
                        "Gagal",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        "Salah satu slot waktu tidak tersedia..",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayMedium,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Rekomendasi Ruangan",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium,
                        color = Green800
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RoomRecommendation("Ruang 3", "23 Maret 2025","16.00 s/d 17.00")
                        RoomRecommendation("Ruang 4", "23 Maret 2025","15.00 s/d 16.00")
                        RoomRecommendation("Ruang Danantara", "23 Maret 2025","15.00 s/d 16.00")
                    }
                }
            }
        }
    }
}
@Composable
fun RoomRecommendation(roomName: String, date: String ,time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, Color.Black, shape = RoundedCornerShape(4.dp))
                .padding(8.dp).fillMaxWidth(0.6f),
        ) {
            Text(
                text = roomName,
                style = MaterialTheme.typography.titleMedium,
                color = Green800,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = date,
                style = MaterialTheme.typography.displayMedium,
            )
            Text(
                text = time,
                style = MaterialTheme.typography.displayMedium,
            )
        }
        Button(
            onClick = { /* TODO: Handle booking */ },
            shape = RoundedCornerShape(8.dp)
            ) {
            Text(text = "Pesan")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Preview() {
    SirasaTheme {
        DialogBooking { true }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun PreviewUserHome() {
//    SirasaTheme {
//        UserHomeScreen()
//    }
//}