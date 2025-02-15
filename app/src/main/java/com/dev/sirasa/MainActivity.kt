package com.dev.sirasa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.sirasa.screens.common.email_verification.VerifiedAccountScreen
import com.dev.sirasa.screens.common.forget_password.ResetPasswordScreen
import com.dev.sirasa.screens.common.login.LoginScreen
import com.dev.sirasa.screens.common.register.RegisterScreen
import com.dev.sirasa.screens.user.bottom_nav_bar.BottomNavItem
import com.dev.sirasa.screens.user.bottom_nav_bar.BottomNavUser
import com.dev.sirasa.screens.user.home.UserHomeScreen
import com.dev.sirasa.ui.theme.SirasaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SirasaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                    ) { innerPadding ->
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavUser(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { UserHomeScreen() }
            composable(BottomNavItem.Room.route) { RegisterScreen() }
            composable(BottomNavItem.History.route) { VerifiedAccountScreen() }
            composable(BottomNavItem.Profile.route) { Text("Profile Screen") }
        }
    }
}