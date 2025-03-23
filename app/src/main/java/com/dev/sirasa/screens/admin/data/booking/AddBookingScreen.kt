package com.dev.sirasa.screens.admin.data.booking

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.dev.sirasa.data.remote.response.booking.CreateBookingRequest
import com.dev.sirasa.data.remote.response.booking.DataBooking
import com.dev.sirasa.data.remote.response.booking.DataRecommendation
import com.dev.sirasa.screens.admin.data.DataViewModel
import com.dev.sirasa.screens.user.history.items
import com.dev.sirasa.screens.user.home.BookingState
import com.dev.sirasa.screens.user.home.DialogBookingFailed
import com.dev.sirasa.screens.user.home.DialogBookingSuccess
import com.dev.sirasa.screens.user.home.UserViewModel
import com.dev.sirasa.ui.component.CustomOptionTime
import com.dev.sirasa.ui.component.DateField
import com.dev.sirasa.ui.component.DropdownField
import com.dev.sirasa.ui.component.InputField
import com.dev.sirasa.ui.component.InputFieldTextArea
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.theme.Typography
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookingScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    userViewModel: UserViewModel = hiltViewModel(),
    dataViewModel: DataViewModel = hiltViewModel()
) {
    var description by remember { mutableStateOf("") }
    var selectedRoomId by remember { mutableStateOf<String?>(null) }
    var selectedRoomName by remember { mutableStateOf("") }
    var selectedSlots by remember { mutableStateOf<List<String>>(emptyList()) }
    var capacity by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    val bookingState by userViewModel.bookingState.collectAsState()
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showFailedDialog by remember { mutableStateOf(false) }
    var bookingData by remember { mutableStateOf<DataBooking?>(null) }
    var recommendationData by remember { mutableStateOf<List<DataRecommendation?>?>(emptyList()) }
    var recommendationMsg by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf(false) }
    var roomError by remember { mutableStateOf(false) }
    var slotError by remember { mutableStateOf(false) }
    var capacityError by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedUser by remember { mutableStateOf("") }
    var selectedUserId by remember { mutableStateOf("") }
    //Data View Model
    val rooms by userViewModel.rooms.collectAsState()
    LaunchedEffect(Unit) {
        userViewModel.resetBookingState()
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
                val data = (bookingState as BookingState.Success).data
                bookingData = data
                showSuccessDialog = true
            }
            is BookingState.Recommendation -> {
                val data = (bookingState as BookingState.Recommendation).data
                val message= (bookingState as BookingState.Recommendation).message
                recommendationData = data
                recommendationMsg = message
                showFailedDialog = true
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

    //DataUser
    val users = dataViewModel.usersState.collectAsLazyPagingItems()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add Booking Data") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Nama Peminjam",
                    style = Typography.displayMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                SelectWithSearch(
                    selectedOption = selectedUser,
                    onUserSelected = { userId, userName ->
                        selectedUserId = userId
                        selectedUser = userName
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                DateField { date ->
                    selectedDate = date
                    dateError = date.isEmpty()
                }
                if (dateError) {
                    Text(
                        text = "Harap pilih tanggal terlebih dahulu",
                        color = Color.Red,
                        style = Typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                DropdownField(
                    "Ruangan",
                    options = rooms.map { it.name },
                    selectedOption = selectedRoomName,
                    onOptionSelected = { selectedName ->
                        selectedRoomName = selectedName
                        selectedRoomId = rooms.find { it.name == selectedName }?.id
                        roomError = selectedRoomId == null
                    }
                )
                if (roomError) {
                    Text(
                        text = "Harap pilih ruangan terlebih dahulu",
                        color = Color.Red,
                        style = Typography.bodySmall,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        CustomOptionTime(
                            if (selectedRoomName.isEmpty() && selectedDate == null) true else false,
                            options = availableSlots,
                            selectedOptions = selectedSlots.mapNotNull { id ->
                                availableSlots.find { it.second == id }?.first
                            },
                            onOptionSelected = { selectedText ->
                                selectedSlots = selectedText
                                if (selectedText.isNotEmpty()) slotError = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (slotError) {
                            Text(
                                text = "Harap pilih slot waktu",
                                color = Color.Red,
                                style = Typography.bodySmall,
                                modifier = Modifier.align(Alignment.Start)
                            )
                        }
                    }
                    Column(modifier = Modifier.width(120.dp)) {
                        InputField(
                            label = "Jumlah Peserta",
                            placeHolder = "Peserta",
                            value = capacity,
                            onValueChange = {
                                capacity = it
                                capacityError = it.isEmpty() },
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (capacityError) {
                            Text(
                                text = "Harus diisi",
                                color = Color.Red,
                                style = Typography.bodySmall,
                                modifier = Modifier.align(Alignment.Start)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                InputFieldTextArea(
                    "Keterangan Keperluan",
                    description,
                    { description = it },
                    KeyboardType.Text
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        dateError = selectedDate == null
                        roomError = selectedRoomId.isNullOrEmpty()
                        slotError = selectedSlots.isEmpty()
                        capacityError = capacity.isEmpty() || capacity.toIntOrNull() == null

                        if (!dateError && !roomError && !slotError && !capacityError) {
                            val request = CreateBookingRequest(
                                roomId = selectedRoomId!!,
                                bookingSlotId = selectedSlots,
                                participant = capacity.toInt(),
                                description = description
                            )
                            userViewModel.createBookingById(selectedUserId, request)
                        }
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
            if (showSuccessDialog && bookingData != null) {
                Log.d("homescreen", "show succces dialog")
                DialogBookingSuccess(data = bookingData!!) {
                    showSuccessDialog = false
                    userViewModel.resetBookingState()
                }
            }
            if (showFailedDialog && recommendationData != null) {
                val request = CreateBookingRequest(
                    roomId = selectedRoomId!!,
                    bookingSlotId = selectedSlots,
                    participant = capacity.toInt(),
                    description = description
                )
                DialogBookingFailed(
                    data = recommendationData!!,
                    message = recommendationMsg,
                    references = request,
                    onDismiss = {
                        showFailedDialog = false
                        userViewModel.resetBookingState() },
                    onBook = { roomId, selectedSlots ->
                        showFailedDialog = false
                        val newRequest = CreateBookingRequest(
                            roomId = roomId,
                            bookingSlotId = selectedSlots,
                            participant = request.participant,
                            description = request.description
                        )
                        userViewModel.createBooking(newRequest)
                    }
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectWithSearch(
    selectedOption: String,
    onUserSelected: (String, String) -> Unit,
    viewModel: DataViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val users = viewModel.usersState.collectAsLazyPagingItems()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(expanded) {
        if (expanded) {
            focusRequester.requestFocus()
            delay(100)
            keyboardController?.show()
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable { expanded = true },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (selectedOption.isNotEmpty()) selectedOption else "Pilih User",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selectedOption.isNotEmpty()) Color.Black else Color.Gray
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            }
        }

        if (expanded) {
            Dialog(onDismissRequest = { expanded = false }) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Nama Peminjam",
                            style = Typography.titleMedium,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            textAlign = TextAlign.Center
                        )
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                viewModel.getUsers(it)
                            },
                            textStyle = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                            placeholder = {
                                Text(
                                    text = "Cari User",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            },
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn {
                            items(users.itemSnapshotList.items) { user ->
                                DropdownMenuItem(
                                    text = { Text("${user.name!!} - ${user.nim!!}") },
                                    onClick = {
                                        onUserSelected(user.id!!, user.name!!)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



