package com.dev.sirasa.screens.admin.data.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.dev.sirasa.R
import com.dev.sirasa.data.remote.response.booking.BookingSlotItem
import com.dev.sirasa.data.remote.response.booking.SlotsItemPaginate
import com.dev.sirasa.data.remote.response.user.DataUser
import com.dev.sirasa.screens.admin.data.DataBookingsState
import com.dev.sirasa.screens.admin.data.DataViewModel
import com.dev.sirasa.screens.admin.data.ExportState
import com.dev.sirasa.screens.admin.data.user.ExportDialogUser
import com.dev.sirasa.screens.admin.data.user.InputFieldSearch
import com.dev.sirasa.screens.user.history.QrCodeDialog
import com.dev.sirasa.ui.component.DateFieldFilter
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.theme.Green300
import com.dev.sirasa.ui.theme.Green700
import com.dev.sirasa.ui.theme.Typography
import com.dev.sirasa.utils.formatDate
import com.dev.sirasa.utils.formatTimeSlot
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataBookingScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: DataViewModel = hiltViewModel(),
    onBack: () -> Unit,
    startDate: String? = null,
    endDate: String? = null,
    status: String? = null
) {
    val bookings = viewModel.bookingState.collectAsLazyPagingItems()
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf(status) }
    val statuses = listOf("all", "booked", "done", "cancel")
    val statusMap = mapOf(
        "all" to "All",
        "booked" to "Dipesan",
        "done" to "Selesai",
        "cancel" to "Cancel"
    )

    var startDate by remember { mutableStateOf<String?>(startDate) }
    var endDate by remember { mutableStateOf<String?>(endDate) }
    var showDialogBooking by remember { mutableStateOf(false) }
    val exportState by viewModel.exportState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(startDate, endDate, selectedStatus, searchQuery) {
        viewModel.getBookings(
            startDate = startDate ?: "",
            endDate = endDate ?: "",
            status = if (selectedStatus == "all") null else selectedStatus,
            search = searchQuery
        )
    }
    if (showDialogBooking) {
        ExportDialogBooking (
            onDismiss = { showDialogBooking = false },
            onDateSelected = { startDate, endDate ->
                viewModel.exportBookingsToExcel(context, startDate, endDate)
            }
        )
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Data Peminjaman") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDialogBooking = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.download_icon),
                            contentDescription = "Download"
                        )
                    }
                }
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
                InputFieldBooking(
                    label = "Search",
                    placeHolder = "Masukkan Nama Pengguna/Ruangan",
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    DateFieldFilter(onDateSelected = { startDate = it }, startDate?.let { formatDatePlaceHolder(it) } ?: "Tanggal Mulai", modifier = Modifier.weight(1f).height(48.dp))
                    Text("-")
                    DateFieldFilter(onDateSelected = { endDate = it }, endDate?.let { formatDatePlaceHolder(it) } ?: "Tanggal Selesai", modifier = Modifier.weight(1f).height(48.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    statuses.forEach { status ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedStatus == status,
                                onClick = { selectedStatus = status }
                            )
                            Text(
                                text = statusMap[status] ?: status,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(bookings.itemCount) { index ->
                        bookings[index]?.let { booking ->
                            val date = formatDate(booking.slots?.firstOrNull()?.date)
                            val timeSlot = formatTimeSlotPaginate(booking.slots)

                            CardHistory(
                                idBooking = booking.id!!,
                                dateBooking = date,
                                name = booking.userName!!,
                                phone = booking.userPhone!!,
                                roomName = booking.roomName!!,
                                timeSlot = timeSlot,
                                location = "Lantai ${booking.roomFloor}",
                                participants = "${booking.participant} Orang",
                                description = booking.description,
                                status = booking.status!!,
                                onDoneBooking = {
                                    viewModel.doneBooking(booking.id)
                                    bookings.refresh()
                                },
                                onCancelBooking = {
                                    viewModel.cancelBooking(booking.id)
                                    bookings.refresh()
                                }
                            )
                        }
                    }

                    when (val refreshState = bookings.loadState.refresh) {
                        is LoadState.Loading -> item {
                            Box(
                                modifier = Modifier.fillMaxSize().weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingCircular(
                                    true,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                        is LoadState.Error -> item {
                        }

                        else -> {}
                    }
                    when (val appendState = bookings.loadState.append) {
                        is LoadState.Loading -> item {
                            Box(
                                modifier = Modifier.fillMaxSize().weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingCircular(
                                    true,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        is LoadState.Error -> item {

                        }
                        else -> {}
                    }
                }
            }
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 12.dp, end = 12.dp),
                onClick = {navController.navigate("add_booking")},
                containerColor = Color.White,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    modifier = Modifier.size(32.dp),
                    tint = Green700
                )
            }
            when (exportState) {
                is ExportState.Loading -> LoadingCircular(true, Modifier.align(Alignment.Center))
                is ExportState.Success -> {
                    LaunchedEffect(Unit) {
                        snackbarHostState.showSnackbar("Download berhasil!")
                    }
                }
                is ExportState.Error -> {
                    val errorMessage = (exportState as ExportState.Error).message
                    LaunchedEffect(Unit) {
                        snackbarHostState.showSnackbar("Terjadi kesalahan: $errorMessage")
                    }
                }
                else -> {}
            }
        }
    }
}
private fun formatDatePlaceHolder(date: String?): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID")) // Sesuaikan format input
        val outputFormat = SimpleDateFormat("dd/M/yyyy", Locale("id", "ID"))
        date?.let { outputFormat.format(inputFormat.parse(it)!!) } ?: ""
    } catch (e: Exception) {
        ""
    }
}
@Composable
fun ExportDialogBooking(
    onDismiss: () -> Unit,
    onDateSelected: (String, String) -> Unit
) {
    var startDate by remember { mutableStateOf<String?>(null) }
    var endDate by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(startDate ?: "", endDate ?: "")
                onDismiss()
            }) {
                Text("Download")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Red)
            }
        },
        title = { Text("Export Data Bookings") },
        text = {
            Column {
                Text("Start Date")
                Spacer(modifier = Modifier.height(6.dp))
                DateFieldFilter(
                    onDateSelected = { selectedDate -> startDate = selectedDate },
                    placeHolder = "Start Date"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("End Date")
                Spacer(modifier = Modifier.height(6.dp))
                DateFieldFilter(
                    onDateSelected = { selectedDate -> endDate = selectedDate },
                    placeHolder = "End Date"
                )
            }
        }
    )
}
@Composable
fun InputFieldBooking(label: String, placeHolder: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.White, RoundedCornerShape(8.dp)),
        placeholder = { Text(text = placeHolder, style = Typography.bodyMedium, color = Color.Gray) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFE2E8F0),
        ),
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Search Icon")
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
}

fun formatTimeSlotPaginate(slots: List<SlotsItemPaginate?>?): String {
    if (slots.isNullOrEmpty()) return "Waktu tidak tersedia"

    val sortedSlots = slots.sortedBy { it?.startTime }

    val startTime = sortedSlots.firstOrNull()?.startTime
    val endTime = sortedSlots.lastOrNull()?.endTime

    return if (startTime != null && endTime != null) {
        "$startTime - $endTime"
    } else {
        "Waktu tidak tersedia"
    }
}

@Composable
fun CardHistory(
    idBooking: String,
    dateBooking: String,
    name: String,
    roomName: String,
    phone: String,
    timeSlot: String,
    location: String,
    participants: String,
    description: String? = null,
    status: String,
    onDoneBooking: (() -> Unit)? = null,
    onCancelBooking: (() -> Unit)? = null
) {
    val (badgeColor, statusText) = when (status) {
        "cancel" -> Color.Red to "Dibatalkan"
        "booked" -> Color.Yellow to "Dipesan"
        "done" -> Green300 to "Selesai"
        else -> Color.Gray to "Unknown"
    }
    var menuExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 12.dp)) {
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
                                    if (onDoneBooking != null) {
                                        onDoneBooking()
                                    }
                                },
                                text = { Text("Done Booking") }
                            )
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
            Text(roomName, style = Typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer,)
            Text(timeSlot, style = Typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(location, style = Typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(participants, style = Typography.bodyMedium)
            if (!description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(description, style = Typography.bodyMedium, maxLines = 2)
            }
        }
    }
}