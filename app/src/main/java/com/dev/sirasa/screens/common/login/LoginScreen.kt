package com.dev.sirasa.screens.common.login
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.R
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun LoginScreen() {
    var selectedOption by remember { mutableIntStateOf(0) }
    val listOption = listOf("Email", "NIM")
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    // TabRow dengan bentuk kotak rounded 16dp
    var email by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf("") }
    var nimError by remember { mutableStateOf("") }
    fun validateEmail(input: String) {
        emailError = if (input.isEmpty()) {
            "Email tidak boleh kosong"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            "Email tidak valid"
        } else {
            ""
        }
    }

    fun validateNIM(input: String) {
        nimError = if (input.isEmpty()) {
            "NIM tidak boleh kosong"
        } else if (!input.all { it.isDigit() }) {
            "NIM hanya boleh angka"
        } else {
            ""
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.perpus_logo),
            contentDescription = "Logo Sirasa",
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(80.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Welcome Message
        Text(text = "Selamat Datang", style = Typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Masuklah ke akun Anda untuk melanjutkan.", style = Typography.displayMedium)
        Spacer(modifier = Modifier.height(32.dp))

        TabRow(
            selectedTabIndex = selectedOption,
            modifier = Modifier
                .height(36.dp)
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .clip(RoundedCornerShape(16))
                .border(1.dp, Color(0xFFE2E8F0)),
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedOption])
                        .height(4.dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        ) {
            listOption.forEachIndexed { index, text ->
                Tab(
                    selected = selectedOption == index,
                    onClick = {
                        selectedOption = index
                        emailError = ""
                        nimError = ""
                    },
                    modifier = Modifier.background(
                        if (selectedOption == index) MaterialTheme.colorScheme.primary else Color.White
                    )
                ) {
                    Text(
                        text = text,
                        color = if (selectedOption == index) Color.White else Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display selected option text
        Text(
            text = listOption[selectedOption],
            style = Typography.bodyMedium,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 10.dp)
        )

        OutlinedTextField(
            value = if (selectedOption == 0) email else nim,
            onValueChange = {newValue ->
                if (selectedOption == 0) {
                    email = newValue
                    validateEmail(newValue)
                } else {
                    if (newValue.all { it.isDigit() }) {
                        nim = newValue
                    }
                    validateNIM(newValue)
                }
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = {
                Text(
                    text = "Masukkan ${listOption[selectedOption]} Anda",
                    style = Typography.bodyMedium,
                    color = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (selectedOption == 0) KeyboardType.Email else KeyboardType.Number
            ),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE2E8F0),
            ),
        )

        if (selectedOption == 0 && emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = Color.Red,
                style = Typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp).align(Alignment.Start)
            )
        } else if (selectedOption == 1 && nimError.isNotEmpty()) {
            Text(
                text = nimError,
                color = Color.Red,
                style = Typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp).align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        Text(
            text = "Password",
            style = Typography.bodyMedium,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 10.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE2E8F0),
            ),
            placeholder = { Text("Masukkan Password Anda", style = Typography.bodyMedium, color = Color.Gray) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = if (passwordVisible) R.drawable.ic_lock_show_password else R.drawable.ic_lock_hide_password),
                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Lupa Password?",
            color = MaterialTheme.colorScheme.primary,
            style = Typography.bodyMedium,
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = { /* TODO: Implementasi Login */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Masuk")
        }
        Spacer(modifier = Modifier.height(7.dp))

        // Register Prompt
        Row {
            Text(text = "Belum mempunyai akun?", style = Typography.bodyMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Daftar",
                style = Typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    SirasaTheme {
        LoginScreen()
    }
}