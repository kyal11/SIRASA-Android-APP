package com.dev.sirasa.screens.common.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dev.sirasa.MainViewModel
import com.dev.sirasa.R
import com.dev.sirasa.data.remote.response.user.UpdateAccount
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showImageDialog by remember { mutableStateOf(false) }
    var editingField by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val scrollState = rememberScrollState()
    val profileState by viewModel.profileState.collectAsState()
    val userData by viewModel.userData.collectAsState()
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
        }
    }
    LaunchedEffect(profileState) {
        Log.d("Logout", "Current profileState: $profileState")
        when (profileState) {
            is ProfileState.Error -> {
                val errorMessage = (profileState as ProfileState.Error).message
                snackbarHostState.showSnackbar(message = errorMessage, actionLabel = "OK")
                viewModel.resetProfileState()
            }
            is ProfileState.Success -> {
                val successMessage = (profileState as ProfileState.Success).message
                Log.d("Logout", "msg success profile: $successMessage")
                snackbarHostState.showSnackbar(message = successMessage, actionLabel = "Tutup")
                viewModel.resetProfileState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { navController.navigate("contact_screen") },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.contact_icon),
                    contentDescription = "Contact",
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(
                onClick = { navController.navigate("faq_screen") },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.faq_icon),
                    contentDescription = "FAQ",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Profile Image
        Box(
            modifier = Modifier.clickable { showImageDialog = true }
        ) {
            if (userData?.imageUrl != null) {
                Column {

                }
                AsyncImage(
                    model = "https://sirasa.teamteaguard.com${userData?.imageUrl}",
                    contentDescription = "Photo Profile",
                    modifier = Modifier
                        .clip(RoundedCornerShape(180.dp))
                        .size(120.dp),
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
                        .size(120.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = userData?.name ?: "",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = userData?.email ?: "",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileDetail(
                    label = "Name",
                    value = userData?.name ?: "",
                    onEdit = {
                        editingField = "name"
                        showEditDialog = true
                    }
                )
                ProfileDetail(
                    label = "Email",
                    value = userData?.email ?: ""
                )
                ProfileDetail(
                    label = "NIM",
                    value = userData?.nim ?: ""
                )
                ProfileDetail(
                    label = "No Telepon",
                    value = userData?.phoneNumber ?: "",
                    onEdit = {
                        editingField = "phoneNumber"
                        showEditDialog = true
                    }
                )
                ProfileDetail(
                    label = "Password",
                    value = "***********",
                    onEdit = {
                        editingField = "password"
                        showEditDialog = true
                    }
                )
            }
        }

        Button(
            onClick = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        ) {
            Text("Logout", style = Typography.titleMedium)
        }
    }
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
                AsyncImage(
                    model = selectedImageUri ?: userData?.imageUrl?.let { "https://sirasa.teamteaguard.com$it" } ?: R.drawable.profile_user,
                    contentDescription = "Expanded Profile Photo",
                    modifier = Modifier
                        .size(250.dp)
                        .clip(CircleShape),
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
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(2.dp, Color(0xFFD1D5DB)),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 4.dp
                        ),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                    ) {
                        Text("Pilih Gambar", fontWeight = FontWeight.Bold)
                    }
                    if (selectedImageUri != null) {
                        Button(
                            onClick = {
                                selectedImageUri?.let { uri ->
                                    viewModel.updateProfileImage(context, uri)
                                    showImageDialog = false
                                    selectedImageUri = null
                                }
                            },
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
    if (showEditDialog) {
        EditProfileDialog(
            field = editingField,
            currentValue = when (editingField) {
                "name" -> userData?.name ?: ""
                "nim" -> userData?.nim ?: ""
                "phoneNumber" -> userData?.phoneNumber ?: ""
                "password" -> ""
                else -> ""
            },
            onDismiss = { showEditDialog = false },
            onSave = { newValue ->
                val updateData = when (editingField) {
                    "name" -> UpdateAccount(name = newValue)
                    "nim" -> UpdateAccount(nim = newValue)
                    "phoneNumber" -> UpdateAccount(phoneNumber = newValue)
                    "password" -> UpdateAccount(password = newValue)
                    else -> UpdateAccount()
                }
                viewModel.updateAccount(updateData)
                showEditDialog = false
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to logout?") },
            containerColor = Color.White,
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout()
                    navController.navigate("auth_screen")
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Color.Red)
                }
            }
        )
    }
}

@Composable
fun ProfileDetail(label: String, value: String, onEdit: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = label, style = MaterialTheme.typography.labelMedium)
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
        onEdit?.let {
            IconButton(onClick = it) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit $label")
            }
        }
    }
}

@Composable
fun EditProfileDialog(field: String, currentValue: String, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var newValue by remember { mutableStateOf(currentValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit $field") },
        text = {
            TextField(
                value = newValue,
                onValueChange = { newValue = it },
                label = { Text(field) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp)), // Mirip OutlinedTextField
                shape = RoundedCornerShape(8.dp), // Mengatur rounded corner
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White, // Background saat tidak fokus
                    focusedContainerColor = Color.White, // Background saat fokus
                    unfocusedIndicatorColor = Color(0xFFE2E8F0), // Warna border saat tidak fokus
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Warna border saat fokus
                    cursorColor = MaterialTheme.colorScheme.primary // Warna kursor
                )
            )

        },
        confirmButton = {
            TextButton(onClick = { onSave(newValue) }) {
                Text("Save")
            }
        },
        containerColor = Color.White,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewProfile() {
//    SirasaTheme {
//        ProfileScreen()
//    }
//}
