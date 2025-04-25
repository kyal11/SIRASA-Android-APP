package com.dev.sirasa.screens.admin.data.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.sirasa.R
import com.dev.sirasa.data.remote.response.user.CreateUser
import com.dev.sirasa.screens.admin.data.DataAddUserState
import com.dev.sirasa.screens.admin.data.DataViewModel
import com.dev.sirasa.screens.common.register.RegisterState
import com.dev.sirasa.screens.common.register.RegisterViewModel
import com.dev.sirasa.ui.component.DropdownField
import com.dev.sirasa.ui.component.InputField
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.component.PasswordField
import com.dev.sirasa.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    userRole: String?,
    onBack: () -> Unit,
    viewModel: DataViewModel = hiltViewModel(),
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var role by remember { mutableStateOf("User") }
    var verified by remember { mutableStateOf(false) }
    val roles = listOf("User", "Admin", "Superadmin")

    val addUserState by viewModel.addUserState.collectAsState()

    LaunchedEffect(addUserState) {
        when (addUserState) {
            is DataAddUserState.Success -> {
                snackbarHostState.showSnackbar("User berhasil ditambahkan!")
                navController.popBackStack()
            }
            is DataAddUserState.Error -> {
                val errorMessage = (addUserState as DataAddUserState.Error).message
                snackbarHostState.showSnackbar("Gagal menambahkan user: $errorMessage")
            }
            else -> Unit
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tambah Pengguna") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Column {
                InputField(label = "Nama Lengkap", placeHolder = "Masukkan Nama Lengkap", value = name, onValueChange = { name = it })
                InputField(label = "Email", placeHolder = "Masukkan Email", value = email, onValueChange = { email = it }, keyboardType = KeyboardType.Email)
                InputField(label = "Nomor Telepon", placeHolder = "Masukkan No Telepon", value = phoneNumber, onValueChange = { phoneNumber = it }, keyboardType = KeyboardType.Phone)
                InputField(label = "NIM", placeHolder = "Masukkan NIM", value = nim, onValueChange = { nim = it }, keyboardType = KeyboardType.Number)
                PasswordField(label = "Password", value = password, onValueChange = { password = it }, passwordVisible = passwordVisible, onTogglePassword = { passwordVisible = !passwordVisible })
                if (userRole == "superadmin") {
                    DropdownField(label= "Role", options = roles, selectedOption = role, onOptionSelected = { role = it })
                }

                Row(
                    modifier = Modifier.align(Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Verified", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = !verified, onClick = { verified = false })
                    Text(text = "False", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = verified, onClick = { verified = true })
                    Text(text = "True", style = MaterialTheme.typography.displayMedium)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val roleFilter = when (role) {
                            "Superadmin" -> "superadmin"
                            "Admin" -> "admin"
                            "User" -> "user"
                            else -> null
                        }
                        viewModel.createUser(
                            CreateUser(
                                name = name,
                                email = email,
                                phoneNumber = phoneNumber,
                                nim = nim,
                                password = password,
                                role = roleFilter ?: "user",
                                verified = verified
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    enabled = addUserState !is DataAddUserState.Loading
                ) {
                    if (addUserState is DataAddUserState.Loading) {
                        LoadingCircular(true, Modifier)
                    } else {
                        Text(text = "Tambah Pengguna")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
