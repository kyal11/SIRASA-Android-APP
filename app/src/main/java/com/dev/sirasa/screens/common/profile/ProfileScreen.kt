package com.dev.sirasa.screens.common.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.sirasa.MainViewModel
import com.dev.sirasa.R
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun ProfileScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState ,
    viewModel: ProfileViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editingField by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("John Doe") }
    var phoneNumber by remember { mutableStateOf("+62 812 3456 7890") }
    var password by remember { mutableStateOf("********") }
    val profileState by viewModel.profileState.collectAsState()

    LaunchedEffect(profileState) {
        Log.d("Logout", "Current profileState: $profileState")
        when (profileState) {
            is ProfileState.Success -> {
                mainViewModel.checkUserSession()
            }
            is ProfileState.Error -> {
                val errorMessage = (profileState as ProfileState.Error).message
                snackbarHostState.showSnackbar(message = errorMessage, actionLabel = "OK")
            }
            else -> {}
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(R.drawable.perpus_logo),
            contentDescription = "Photo Profile",
            modifier = Modifier
                .clip(RoundedCornerShape(180.dp))
                .size(120.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = name, style = MaterialTheme.typography.titleLarge)
        Text(text = "john.doe@example.com", style = MaterialTheme.typography.bodyMedium)
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
                ProfileDetail(label = "Name", value = name, onEdit = { editingField = "name"; showEditDialog = true })
                ProfileDetail(label = "Email", value = "john.doe@example.com")
                ProfileDetail(label = "NIM", value = "123456789")
                ProfileDetail(label = "Phone Number", value = phoneNumber, onEdit = { editingField = "phone"; showEditDialog = true })
                ProfileDetail(label = "Password", value = password, onEdit = { editingField = "password"; showEditDialog = true })
            }
        }
        Button(
            onClick = { showLogoutDialog = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 64.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        )
        {
            Text("Logout", style = Typography.titleMedium)
        }
    }

    if (showEditDialog) {
        EditProfileDialog(
            field = editingField,
            currentValue = when (editingField) {
                "name" -> name
                "phone" -> phoneNumber
                "password" -> password
                else -> ""
            },
            onDismiss = { showEditDialog = false },
            onSave = { newValue ->
                when (editingField) {
                    "name" -> name = newValue
                    "phone" -> phoneNumber = newValue
                    "password" -> password = newValue
                }
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
            TextField(value = newValue, onValueChange = { newValue = it }, label = { Text(field) })
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
