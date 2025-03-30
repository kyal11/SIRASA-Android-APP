package com.dev.sirasa.screens.admin.data.user

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.dev.sirasa.R
import com.dev.sirasa.data.remote.response.user.DataUser
import com.dev.sirasa.screens.admin.data.DataViewModel
import com.dev.sirasa.screens.admin.data.ExportState
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.component.convertMillisToApiFormat
import com.dev.sirasa.ui.theme.Green700
import com.dev.sirasa.ui.theme.Typography
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.dev.sirasa.ui.component.DateFieldFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataUserScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    userRole: String?,
    viewModel: DataViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(if (userRole == "admin") "User" else "All") }
    val roles = listOf("All", "Super Admin", "Admin", "User")
    val exportState by viewModel.exportState.collectAsState()
    val users = viewModel.usersState.collectAsLazyPagingItems()
    var showDialogUser by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(searchQuery, selectedRole) {
        val roleFilter = when (selectedRole) {
            "Super Admin" -> "superadmin"
            "Admin" -> "admin"
            "User" -> "user"
            else -> null
        }
        viewModel.getUsers(searchQuery, roleFilter)
    }
    if (showDialogUser) {
        ExportDialogUser(
            onDismiss = { showDialogUser = false },
            onDateSelected = { startDate, endDate ->
                viewModel.exportUsersToExcel(context, startDate, endDate)
            }
        )
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Data Users"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDialogUser = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.download_icon),
                            contentDescription = "Download"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputFieldSearch(
                    label = "Search",
                    placeHolder = "Enter name",
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth()
                )

                // Role Filter
                if (userRole == "superadmin") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        roles.forEach { role ->
                            Button(
                                onClick = { selectedRole = role },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedRole == role) MaterialTheme.colorScheme.primary else Color.White,
                                    contentColor = if (selectedRole == role) Color.White else Color.Black
                                ),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Text(text = role)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // User List menggunakan Paging3
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(users.itemCount) { index ->
                        val user = users[index]
                        if (user != null) {
                            CardUser(user,onClick = {
                                navController.navigate("profile/${user.id}")
                            })
                        }
                    }

                    // Loading & Error States
                    users.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                item {
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
                            }

                            loadState.append is LoadState.Loading -> {
                                item {
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
                            }

                            loadState.refresh is LoadState.Error -> {
                                val error = (loadState.refresh as LoadState.Error).error
                                item {
                                    Text(
                                        text = "Error: ${error.message}",
                                        color = Color.Red,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 12.dp, end = 12.dp),
                onClick = {navController.navigate("add_user")},
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
@Composable
fun ExportDialogUser(
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
        title = { Text("Export Data Users") },
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
fun InputFieldSearch(label: String, placeHolder: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text, modifier: Modifier = Modifier) {
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
@Composable
fun CardUser(user: DataUser, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            if (user?.imageUrl != null) {
                AsyncImage(
                    model = "https://sirasa.teamteaguard.com${user?.imageUrl}",
                    contentDescription = "Photo Profile",
                    modifier = Modifier
                        .clip(RoundedCornerShape(180.dp))
                        .size(50.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.profile_user),
                    error = painterResource(R.drawable.profile_user)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.profile_user),
                    contentDescription = "Photo Profile",
                    modifier = Modifier
                        .clip(RoundedCornerShape(180.dp))
                        .size(50.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name ?: "No Name", style = Typography.titleMedium)
                Text(text = "NIM: ${user.nim ?: "-"}", style = Typography.bodyMedium)
                Text(text = "Phone: ${user.phoneNumber ?: "-"}", style = Typography.bodyMedium)
            }
        }
    }
}