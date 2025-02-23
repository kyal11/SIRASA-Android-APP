package com.dev.sirasa.screens.common.forget_password

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dev.sirasa.ui.theme.Typography
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.sirasa.ui.component.InputField
import com.dev.sirasa.ui.component.PasswordField
import com.dev.sirasa.ui.theme.SirasaTheme
import kotlinx.coroutines.delay

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val resetPasswordState by viewModel.resetPasswordState.collectAsState()
    var isButtonEnabled by remember { mutableStateOf(true) }
    var timer by remember { mutableIntStateOf(0) }
    var email by remember { mutableStateOf("") }
    LaunchedEffect(timer) {
        if (timer > 0) {
            delay(1000L)
            timer--
            if (timer == 0) isButtonEnabled = true
        }
    }
    LaunchedEffect(resetPasswordState) {
        when (resetPasswordState) {
            is ResetPasswordState.Success -> {
                snackbarHostState.showSnackbar("Email reset password berhasil dikirim.")
            }
            is ResetPasswordState.Error -> {
                val errorMessage = (resetPasswordState as ResetPasswordState.Error).message
                snackbarHostState.showSnackbar("Gagal mengirim email: $errorMessage")
                isButtonEnabled = true
            }
            else -> {}
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Reset Password", style = Typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Masukkan email yang terdaftar untuk menerima tautan reset password.",
                style = Typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            InputField(
                label = "Email",
                placeHolder = "Masukkan Email",
                value = email,
                onValueChange = { email = it },
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isButtonEnabled = false
                    timer = 60
                    viewModel.sendEmail(email)
                },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Kirim Permintaan")
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    token: String,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    val resetPasswordState by viewModel.resetPasswordState.collectAsState()
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(resetPasswordState) {
        when (resetPasswordState) {
            is ResetPasswordState.Success -> {
                snackbarHostState.showSnackbar("Password berhasil diubah.")
                navController.navigate("login")
            }
            is ResetPasswordState.Error -> {
                val errorMessage = (resetPasswordState as ResetPasswordState.Error).message
                    snackbarHostState.showSnackbar("Gagal: $errorMessage")
            }
            else -> {}
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        IconButton(onClick = { navController.navigate("login") }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Ubah Password", style = Typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Masukkan password baru kamu dan pastikan sesuai.",
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Input Password Baru
            PasswordField(
                label = "Password Baru",
                value = newPassword,
                onValueChange = { newPassword = it },
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )

            // Input Konfirmasi Password
            PasswordField(
                label = "Konfirmasi Password",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                passwordVisible = confirmPasswordVisible,
                onTogglePassword = { confirmPasswordVisible = !confirmPasswordVisible }
            )

            // Button Simpan Password
            Button(
                onClick = { viewModel.resetPassword(token, newPassword, confirmPassword) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Simpan Password")
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewResetPassword() {
//    SirasaTheme {
//        ResetPasswordScreen(
//            onBack = {},
//            onEmailSent = {}
//        )
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun PreviewChangePassword() {
//    SirasaTheme {
//        ChangePasswordScreen(
//            onBack = {},
//            onPasswordChanged = {}
//        )
//    }
//}
