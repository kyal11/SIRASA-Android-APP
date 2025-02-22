package com.dev.sirasa

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.sirasa.screens.admin.bottom_nav_bar.BottomNavAdmin
import com.dev.sirasa.screens.admin.bottom_nav_bar.BottomNavItemAdmin
import com.dev.sirasa.screens.admin.dashboard.DashboardScreen
import com.dev.sirasa.screens.admin.data.DataScreen
import com.dev.sirasa.screens.common.email_verification.VerifiedAccountScreen
import com.dev.sirasa.screens.common.forget_password.ResetPasswordScreen
import com.dev.sirasa.screens.common.login.LoginScreen
import com.dev.sirasa.screens.common.login.LoginViewModel
import com.dev.sirasa.screens.common.profile.ProfileScreen
import com.dev.sirasa.screens.common.register.RegisterScreen
import com.dev.sirasa.screens.user.bottom_nav_bar.BottomNavItemUser
import com.dev.sirasa.screens.user.bottom_nav_bar.BottomNavUser
import com.dev.sirasa.screens.user.history.UserHistoryScreen
import com.dev.sirasa.screens.user.home.UserHomeScreen
import com.dev.sirasa.screens.user.room.UserRoomScreen
import com.dev.sirasa.ui.theme.SirasaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d("Permission", "Notification permission granted.")
            } else {
                Log.d("Permission", "Notification permission denied.")
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if we already have the notification permission
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission already granted
            } else {
                // Request the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        setContent {
            SirasaTheme {
                AuthScreen()
            }
        }
    }
}

@Composable
fun AuthScreen() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "login",
            ) {
                composable("login") { LoginScreen(navController, snackbarHostState) }
                composable("register") { RegisterScreen(navController, snackbarHostState) }
                composable("forget_password") { ResetPasswordScreen(navController) }
                composable("verified_account") { VerifiedAccountScreen() }
            }
        }
    }
}


@Composable
fun MainScreenUser() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavUser(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItemUser.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItemUser.Home.route) { UserHomeScreen() }
            composable(BottomNavItemUser.Room.route) { UserRoomScreen() }
            composable(BottomNavItemUser.History.route) { UserHistoryScreen() }
            composable(BottomNavItemUser.Profile.route) { ProfileScreen() }
        }
    }
}

@Composable
fun MainScreenAdmin() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavAdmin(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItemAdmin.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItemAdmin.Home.route) { DashboardScreen() }
            composable(BottomNavItemAdmin.Room.route) { UserRoomScreen() }
            composable(BottomNavItemAdmin.Data.route) { DataScreen() }
            composable(BottomNavItemAdmin.Profile.route) { ProfileScreen() }
        }
    }
}