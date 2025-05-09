package com.dev.sirasa.screens.common.login
import android.util.Log
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.sirasa.MainViewModel
import com.dev.sirasa.R
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.component.PasswordField
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun LoginScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: LoginViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    var selectedOption by remember { mutableIntStateOf(0) }
    val listOption = listOf("Email", "NIM")
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var nimError by remember { mutableStateOf("") }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                navController.navigate((loginState as LoginState.Success).destination) {
                    popUpTo("login") { inclusive = true }
                }
            }
            is LoginState.Error -> {
                val errorMessage = (loginState as LoginState.Error).message
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    actionLabel = "OK"
                )
            }
            else -> { }
        }
    }
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sirasa),
                contentDescription = "Logo Sirasa",
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

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
                maxLines = 1,
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
                    keyboardType = if (selectedOption == 0) KeyboardType.Email else KeyboardType.Number,
                    imeAction = ImeAction.Done
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
            PasswordField(label = "Password", value = password, onValueChange = { password = it }, passwordVisible = passwordVisible, onTogglePassword = { passwordVisible = !passwordVisible })
            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = {
                    navController.navigate("forget_password")
                },
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.height(20.dp).align(Alignment.End)
            ) {
                Text(
                    text = "Lupa Password?",
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Login Button
            Button(
                onClick = {
                    viewModel.login(
                        email.takeIf { selectedOption == 0 },
                        nim.takeIf { selectedOption == 1 },
                        password
                    ) { role ->
                        mainViewModel.setUserRole(role)
                    }
                },
                enabled = (if (loginState is LoginState.Loading) false else true),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Masuk")
            }
            Spacer(modifier = Modifier.height(7.dp))
            // Register Prompt
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Belum mempunyai akun?", style = Typography.bodyMedium)
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = {
                        navController.navigate("register")
                    },
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.height(20.dp).width(40.dp)
                ) {
                    Text(
                        text = "Daftar",
                        style = Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        if (loginState is LoginState.Loading) {
            LoadingCircular(true, modifier = Modifier.align(Alignment.Center))
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewLogin() {
//    SirasaTheme {
//        LoginScreen()
//    }
//}