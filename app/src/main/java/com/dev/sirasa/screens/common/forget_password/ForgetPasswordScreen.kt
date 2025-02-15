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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.ui.component.InputField
import com.dev.sirasa.ui.component.PasswordField
import com.dev.sirasa.ui.theme.SirasaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(onBack: () -> Unit, onEmailSent: () -> Unit) {
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
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
                .padding(innerPadding)
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

            InputField(label = "Email", value = email, onValueChange = { email = it }, keyboardType = KeyboardType.Email)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onEmailSent() },
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
fun ChangePasswordScreen(onBack: () -> Unit, onPasswordChanged: () -> Unit) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { },
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
                .padding(innerPadding)
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
                onClick = { onPasswordChanged() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Simpan Password")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewResetPassword() {
    SirasaTheme {
        ResetPasswordScreen(
            onBack = {},
            onEmailSent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChangePassword() {
    SirasaTheme {
        ChangePasswordScreen(
            onBack = {},
            onPasswordChanged = {}
        )
    }
}
