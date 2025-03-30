package com.dev.sirasa.screens.admin.data.user

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dev.sirasa.R
import com.dev.sirasa.data.remote.response.user.UpdateAccount
import com.dev.sirasa.screens.admin.data.DataUserDetailState
import com.dev.sirasa.screens.admin.data.DataViewModel
import com.dev.sirasa.screens.admin.data.UpdateUserState
import com.dev.sirasa.screens.common.profile.EditProfileDialog
import com.dev.sirasa.screens.common.profile.ProfileDetail
import com.dev.sirasa.screens.common.profile.ProfileState
import com.dev.sirasa.screens.common.profile.ProfileViewModel
import com.dev.sirasa.ui.theme.Typography
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailUserScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    userRole: String?,
    userId: String,
    onBack: () -> Unit,
    dataViewModel: DataViewModel = hiltViewModel()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showImageDialog by remember { mutableStateOf(false) }
    var editingField by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val updateUserState by dataViewModel.updateUserState.collectAsState()
    val userDetailState by dataViewModel.detailUserState.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri = it }
    }

    // Fungsi untuk me-refresh data user
    val refreshUserData = {
        dataViewModel.getUserById(userId)
    }

    // Observer untuk profile state
    LaunchedEffect(updateUserState) {
        when (updateUserState) {
            is UpdateUserState.Success -> {
                // Refresh data setelah berhasil update
                refreshUserData()
                snackbarHostState.showSnackbar("Update berhasil")
            }
            is UpdateUserState.Error -> {
                val errorMessage = (updateUserState as UpdateUserState.Error).message
                snackbarHostState.showSnackbar("Error: $errorMessage")
            }
            else -> {}
        }
    }

    // Observer untuk user detail state
    LaunchedEffect(userDetailState) {
        when (userDetailState) {
            is DataUserDetailState.Error -> {
                val errorMessage = (userDetailState as DataUserDetailState.Error).message
                snackbarHostState.showSnackbar("Error: $errorMessage")
            }
            else -> {}
        }
    }

    // Load user data saat komponen dimuat
    LaunchedEffect(userId) {
        refreshUserData()
    }

    val userData = when (userDetailState) {
        is DataUserDetailState.Success -> (userDetailState as DataUserDetailState.Success).data
        else -> null
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (userDetailState) {
                is DataUserDetailState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DataUserDetailState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        // Profile Image
                        Box(modifier = Modifier.clickable { showImageDialog = true }) {
                            AsyncImage(
                                model = userData?.imageUrl?.let { "https://sirasa.teamteaguard.com$it" }
                                    ?: R.drawable.profile_user,
                                contentDescription = "Photo Profile",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(180.dp))
                                    .size(120.dp),
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(R.drawable.profile_user),
                                error = painterResource(R.drawable.profile_user)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = userData?.name ?: "John Doe", style = MaterialTheme.typography.titleLarge)
                        Text(text = userData?.email ?: "john.doe@example.com", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                ProfileDetail(label = "Name", value = userData?.name ?: "John Doe") {
                                    editingField = "name"
                                    showEditDialog = true
                                }
                                ProfileDetail(label = "Email", value = userData?.email ?: "john.doe@example.com")  {
                                    editingField = "email"
                                    showEditDialog = true
                                }
                                ProfileDetail(label = "NIM", value = userData?.nim ?: "123456789")  {
                                    editingField = "nim"
                                    showEditDialog = true
                                }
                                ProfileDetail(label = "No Telepon", value = userData?.phoneNumber ?: "Nope") {
                                    editingField = "phoneNumber"
                                    showEditDialog = true
                                }
                                ProfileDetail(label = "Password", value = "***********") {
                                    editingField = "password"
                                    showEditDialog = true
                                }
                                ProfileDetail(label = "Verified", value = userData?.verified.toString()) {
                                    editingField = "verified"
                                    showEditDialog = true
                                }
                                ProfileDetail(label = "Role", value = userData?.role ?: "User", onEdit = if (userRole == "superadmin") {
                                    {
                                        editingField = "role"
                                        showEditDialog = true
                                    }
                                } else null)

                            }
                        }
                        if (userRole == "superadmin") {
                            Button(
                                onClick = { showDeleteDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 64.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Delete", style = Typography.titleMedium)
                            }
                        }
                    }
                }
                is DataUserDetailState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Error: ${(userDetailState as DataUserDetailState.Error).message}")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = refreshUserData) {
                            Text("Coba Lagi")
                        }
                    }
                }
                else -> {
                    // Idle state - mungkin tampilkan placeholder atau loading
                }
            }

            // Tampilkan loading indicator jika sedang update profile
            if (updateUserState is UpdateUserState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    // Edit Dialog - Verified Status
    if (showEditDialog && editingField == "verified") {
        var selectedVerified by remember { mutableStateOf(userData?.verified ?: false) }
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Verified Status") },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedVerified,
                            onClick = { selectedVerified = true }
                        )
                        Text("Verified")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = !selectedVerified,
                            onClick = { selectedVerified = false }
                        )
                        Text("Not Verified")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    userData?.id?.let { id ->
                        dataViewModel.updateAccount(id, UpdateAccount(verified = selectedVerified))
                        showEditDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Dialog - Role
// Edit Dialog - Role
    if (showEditDialog && editingField == "role") {
        var selectedRole by remember {
            mutableStateOf(
                when (userData?.role) {
                    "superadmin" -> "superadmin"
                    "admin" -> "admin"
                    "user" -> "user"
                    else -> "user"
                }
            )
        }
        val roles = listOf("User", "Admin", "Super Admin")

        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Role") },
            text = {
                Column {
                    roles.forEach { role ->
                        val roleFilter = when (role) {
                            "Super Admin" -> "superadmin"
                            "Admin" -> "admin"
                            "User" -> "user"
                            else -> null
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedRole == roleFilter, // Sekarang cocok
                                onClick = { selectedRole = roleFilter ?: "user" }
                            )
                            Text(role)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    userData?.id?.let { id ->
                        dataViewModel.updateAccount(id, UpdateAccount(role = selectedRole))
                        showEditDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Dialog for Image
    if (showImageDialog) {
        Dialog(onDismissRequest = {
            showImageDialog = false
            selectedImageUri = null
        }) {
            Column(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tampilkan gambar yang dipilih atau gambar profil saat ini
                AsyncImage(
                    model = selectedImageUri ?: userData?.imageUrl?.let { "https://sirasa.teamteaguard.com$it" } ?: R.drawable.profile_user,
                    contentDescription = "Expanded Profile Photo",
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Gallery Button
                    Button(
                        onClick = {
                            imagePickerLauncher.launch("image/*")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Select from Gallery")
                    }
                    if (selectedImageUri != null) {
                        Button(
                            onClick = {
                                selectedImageUri?.let { uri ->
                                    userData?.id?.let { id ->
                                        dataViewModel.updateProfileImage(id, context, uri)
                                        showImageDialog = false
                                        selectedImageUri = null
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Green
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    // Edit Dialog - Other fields
    if (showEditDialog && editingField !in listOf("verified", "role")) {
        EditProfileDialog(
            field = editingField,
            currentValue = when (editingField) {
                "name" -> userData?.name ?: ""
                "nim" -> userData?.nim ?: ""
                "phoneNumber" -> userData?.phoneNumber ?: ""
                "password" -> ""
                "email" -> userData?.email ?: ""
                "nim" -> userData?.nim ?: ""
                else -> ""
            },
            onDismiss = { showEditDialog = false },
            onSave = { newValue ->
                val updateData = when (editingField) {
                    "name" -> UpdateAccount(name = newValue)
                    "nim" -> UpdateAccount(nim = newValue)
                    "phoneNumber" -> UpdateAccount(phoneNumber = newValue)
                    "password" -> UpdateAccount(password = newValue)
                    "email" -> UpdateAccount(email = newValue)
                    "nim" -> UpdateAccount(nim = newValue)
                    else -> UpdateAccount()
                }
                userData?.id?.let { id ->
                    dataViewModel.updateAccount(id, updateData)
                    showEditDialog = false
                }
            }
        )
    }

    // Delete Dialog
    if (showDeleteDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete this user? This action cannot be undone.") },
            confirmButton = {
                Button(onClick = {
                    userData?.id?.let { id ->
                        dataViewModel.deleteUserById(id)
                        scope.launch {
                            snackbarHostState.showSnackbar("User berhasil dihapus")
                        }
                        navController.popBackStack()
                    }
                }) {
                    Text("Yes, Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}
