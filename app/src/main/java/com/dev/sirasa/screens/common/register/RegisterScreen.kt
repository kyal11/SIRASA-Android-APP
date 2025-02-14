package com.dev.sirasa.screens.common.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.R
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun RegisterScreen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var passwordConfirm by remember { mutableStateOf("") }
    var passwordConfirmVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.perpus_logo),
            contentDescription = "Logo Sirasa",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Daftar Sekarang!", style = Typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Akses peminjaman ruangan diskusi perpustakaan UPNVJ",
            style = Typography.displayMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))

        InputField(label = "Nama Lengkap", value = name, onValueChange = { name = it })
        InputField(label = "Email", value = email, onValueChange = { email = it }, keyboardType = KeyboardType.Email)
        InputField(label = "Nomor Telepon", value = phoneNumber, onValueChange = { phoneNumber = it }, keyboardType = KeyboardType.Phone)
        InputField(label = "NIM", value = nim, onValueChange = { nim = it }, keyboardType = KeyboardType.Number)
        PasswordField(label = "Password", value = password, onValueChange = { password = it }, passwordVisible = passwordVisible, onTogglePassword = { passwordVisible = !passwordVisible })
        PasswordField(label = "Konfirmasi Password", value = passwordConfirm, onValueChange = { passwordConfirm = it }, passwordVisible = passwordConfirmVisible, onTogglePassword = { passwordConfirmVisible = !passwordConfirmVisible })
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Implementasi Register */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Daftar")
        }
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = Typography.displayMedium, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = { Text(text = "Masukkan $label", style = Typography.bodyMedium, color = Color.Gray) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE2E8F0),
            ),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun PasswordField(label: String, value: String, onValueChange: (String) -> Unit, passwordVisible: Boolean, onTogglePassword: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = Typography.bodyMedium, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE2E8F0),
            ),
            placeholder = { Text("Masukkan $label", style = Typography.bodyMedium, color = Color.Gray) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        painter = painterResource(id = if (passwordVisible) R.drawable.ic_lock_show_password else R.drawable.ic_lock_hide_password),
                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    SirasaTheme {
        RegisterScreen()
    }
}