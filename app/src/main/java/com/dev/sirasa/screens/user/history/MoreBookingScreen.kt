package com.dev.sirasa.screens.user.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreBookingScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                    "Riwayat Peminjaman",
                    style = Typography.titleMedium
                )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    CardHistory(
                        idBooking = "12345",
                        dateBooking = "12 Februari 2025",
                        name = "Budi Santoso",
                        phone = "081234567890",
                        timeSlot = "14:00 - 16:00",
                        location = "Ruang Rapat A",
                        participants = "5 Orang",
                        description = "Meeting internal tim proyek",
                        status = "Selesai",
                        onCancelBooking = {} // Bisa dibatalkan
                    )
                    CardHistory(
                        idBooking = "12345",
                        dateBooking = "12 Februari 2025",
                        name = "Yusup Santoso",
                        phone = "081234567890",
                        timeSlot = "14:00 - 16:00",
                        location = "Ruang Rapat A",
                        participants = "5 Orang",
                        description = "Meeting internal tim proyek",
                        status = "Selesai",
                        onCancelBooking = {} // Bisa dibatalkan
                    )
                    CardHistory(
                        idBooking = "12345",
                        dateBooking = "12 Februari 2025",
                        name = "Andi Levi",
                        phone = "081234567890",
                        timeSlot = "14:00 - 16:00",
                        location = "Ruang Rapat A",
                        participants = "5 Orang",
                        description = "Meeting internal tim proyek",
                        status = "Selesai",
                        onCancelBooking = {} // Bisa dibatalkan
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
fun MoreBooking() {
    SirasaTheme {
        MoreBookingScreen({})
    }
}