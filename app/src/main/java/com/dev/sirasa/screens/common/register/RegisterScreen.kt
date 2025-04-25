package com.dev.sirasa.screens.common.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.sirasa.R
import com.dev.sirasa.ui.component.InputField
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography
import com.dev.sirasa.ui.component.PasswordField

@Composable
fun RegisterScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordConfirm by remember { mutableStateOf("") }
    var passwordConfirmVisible by remember { mutableStateOf(false) }
    val registerState by viewModel.registerState.collectAsState()
    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterState.Success -> {
                snackbarHostState.showSnackbar("Registrasi berhasil! Silakan login.")
                navController.navigate("login")
            }
            is RegisterState.Error -> {
                val errorMessage = (registerState as RegisterState.Error).message
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    actionLabel = "OK"
                )
            }
            else -> {}
        }
    }
    Box (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sirasa),
                contentDescription = "Logo Sirasa",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Daftar Sekarang!", style = Typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Akses peminjaman ruangan diskusi perpustakaan UPNVJ",
                style = Typography.displayMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))

            InputField(label = "Nama Lengkap", placeHolder = "Masukkan Nama Lengkap" ,value = name, onValueChange = { name = it }, maxLines = 1)
            InputField(label = "Email",placeHolder = "Masukkan Email" , value = email, onValueChange = { email = it }, keyboardType = KeyboardType.Email, maxLines = 1)
            InputField(label = "Nomor Telepon", placeHolder = "Masukkan No Telepon" ,value = phoneNumber, onValueChange = { phoneNumber = it }, keyboardType = KeyboardType.Phone, maxLines = 1)
            InputField(label = "NIM",placeHolder = "Masukkan NIM" ,value = nim, onValueChange = { nim = it }, keyboardType = KeyboardType.Number, maxLines = 1)
            PasswordField(label = "Password", value = password, onValueChange = { password = it }, passwordVisible = passwordVisible, onTogglePassword = { passwordVisible = !passwordVisible })
            PasswordField(label = "Konfirmasi Password", value = passwordConfirm, onValueChange = { passwordConfirm = it }, passwordVisible = passwordConfirmVisible, onTogglePassword = { passwordConfirmVisible = !passwordConfirmVisible })
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.register(name, email, password, passwordConfirm, nim, phoneNumber)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Daftar")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sudah Mempunyai Akun?", style = Typography.bodyMedium)
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = {
                        navController.navigate("login")
                    },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.height(20.dp)
                ) {
                    Text(
                        text = "Masuk di sini",
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        if (registerState is RegisterState.Loading) {
            LoadingCircular(true, modifier = Modifier.align(Alignment.Center))
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewRegisterScreen() {
//    SirasaTheme {
//        RegisterScreen()
//    }
//}