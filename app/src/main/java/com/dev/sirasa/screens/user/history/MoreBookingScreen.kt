package com.dev.sirasa.screens.user.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography
import com.dev.sirasa.utils.formatDate
import com.dev.sirasa.utils.formatTimeSlot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreBookingScreen(onBack: () -> Unit, viewModel: HistoryMainModel = hiltViewModel()) {
    val historyActive by viewModel.historyActive.collectAsState()
    val activeState by viewModel.activeState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getActiveBooking()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                    "Peminjaman yang sedang berlangsung",
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
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                                    description = booking.description,
                                    status = booking.status,
                                    onCancelBooking = {
                                        viewModel.cancelBooking(booking.id)
                                    }
                                )
                                Spacer(modifier = Modifier.height(4.dp))
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

@Preview()
@Composable
fun MoreBooking() {
    SirasaTheme {
        MoreBookingScreen({})
    }
}