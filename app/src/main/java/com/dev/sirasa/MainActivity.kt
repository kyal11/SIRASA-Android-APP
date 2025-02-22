package com.dev.sirasa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.sirasa.screens.admin.bottom_nav_bar.BottomNavAdmin
import com.dev.sirasa.screens.admin.bottom_nav_bar.BottomNavItemAdmin
import com.dev.sirasa.screens.admin.dashboard.DashboardScreen
import com.dev.sirasa.screens.admin.data.DataScreen
import com.dev.sirasa.screens.common.forget_password.ResetPasswordScreen
import com.dev.sirasa.screens.common.login.LoginScreen
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    NavHost(
        navController = navController,
        startDestination = "login",
    ) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("forget_password") { ResetPasswordScreen(navController)}
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