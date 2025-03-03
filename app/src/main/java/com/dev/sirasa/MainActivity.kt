package com.dev.sirasa

import android.Manifest
import android.app.Activity
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import androidx.navigation.navDeepLink
import com.dev.sirasa.screens.admin.bottom_nav_bar.BottomNavAdmin
import com.dev.sirasa.screens.admin.bottom_nav_bar.BottomNavItemAdmin
import com.dev.sirasa.screens.admin.dashboard.DashboardScreen
import com.dev.sirasa.screens.admin.data.DataScreen
import com.dev.sirasa.screens.common.email_verification.VerifiedAccountScreen
import com.dev.sirasa.screens.common.forget_password.ChangePasswordScreen
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
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.dev.sirasa.screens.user.history.MoreBookingScreen
import com.dev.sirasa.screens.user.history.MoreHistoryScreen
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: MainViewModel by viewModels()
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
            val authState by viewModel.authState.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }
            SirasaTheme {
                when (authState) {
                    is AuthState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is AuthState.Unauthorized -> {
                        AuthScreen(snackbarHostState)
                    }

                    is AuthState.Authorized -> {
                        val route = (authState as AuthState.Authorized).route
                        val navController = rememberNavController()
                        LaunchedEffect(Unit) {
                            navController.navigate(route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                        Scaffold { innerPadding ->
                            NavHost(
                                navController = navController, startDestination = route,
                                modifier = Modifier.padding(innerPadding)
                            ) {
                                composable("main_screen_user") { MainScreenUser(snackbarHostState) }
                                composable("main_screen_admin") { MainScreenAdmin(snackbarHostState) }
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Serializable
data class ResetPasswordRoute(val token: String)
val uri = "https://sirasa.teamteaguard.com/reset-password"

@Composable
fun AuthScreen(snackbarHostState: SnackbarHostState, viewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val intent = (context as? Activity)?.intent
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(intent?.data) {
        intent?.data?.let { uri ->
            Log.d("DeepLink", "Received deep link: $uri")

            val resetToken = uri.getQueryParameter("token")
            val isEmailValidation = uri.toString().contains("validate-email")
            if (resetToken != null && isEmailValidation) {
                viewModel.validateEmail(resetToken) {
                    navController.navigate("main_screen_user") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            } else if (resetToken != null) {
                navController.navigate("reset_password?token=$resetToken") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                composable("forget_password") { ResetPasswordScreen(navController,snackbarHostState) }
                composable("verified_account") { VerifiedAccountScreen(snackbarHostState) }
                composable("main_screen_user") { MainScreenUser(snackbarHostState) }
                composable("main_screen_admin") { MainScreenAdmin(snackbarHostState) }
                composable(
                    route = "reset_password?token={token}",
                    deepLinks = listOf(
                        navDeepLink { uriPattern = "$uri?token={token}" }
                    )
                ) { backStackEntry ->
                    val token = backStackEntry.arguments?.getString("token") ?: ""
                    ChangePasswordScreen(token = token, navController, snackbarHostState)
                }
            }
        }
    }
}

@Composable
fun MainScreenUser(snackbarHostState: SnackbarHostState) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // List of routes where bottom navigation should be hidden
    val routesWithoutBottomBar = listOf("auth_screen", "more_booking", "more_history")

    // Check if bottom bar should be shown for current route
    val showBottomBar = currentRoute !in routesWithoutBottomBar

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavUser(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItemUser.Home.route,
            modifier = Modifier.consumeWindowInsets(innerPadding)
        ) {
            composable(BottomNavItemUser.Home.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    UserHomeScreen(snackbarHostState)
                }
            }
            composable(BottomNavItemUser.Room.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    UserRoomScreen()
                }
            }
            composable(BottomNavItemUser.History.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    UserHistoryScreen(navController)
                }
            }
            composable(BottomNavItemUser.Profile.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    ProfileScreen(navController, snackbarHostState)
                }
            }
            composable("more_history") { MoreHistoryScreen(onBack = { navController.popBackStack() }) }
            composable("more_booking") { MoreBookingScreen(onBack = { navController.popBackStack() }) }
            composable("auth_screen") { AuthScreen(snackbarHostState) }
        }
    }
}

@Composable
fun MainScreenAdmin(snackbarHostState: SnackbarHostState) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavAdmin(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItemAdmin.Home.route,
            modifier = Modifier.consumeWindowInsets(innerPadding)
        ) {
            composable(BottomNavItemAdmin.Home.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    DashboardScreen()
                }
            }
            composable(BottomNavItemAdmin.Room.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    UserRoomScreen()
                }
            }
            composable(BottomNavItemAdmin.Data.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    DataScreen()
                }
            }
            composable(BottomNavItemAdmin.Profile.route) {
                composable(BottomNavItemUser.Profile.route) {
                    Box(
                        modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                    ) {
                        ProfileScreen(navController, snackbarHostState)
                    }
                }
            }
            composable("auth_screen") { AuthScreen(snackbarHostState) }
        }
    }
}