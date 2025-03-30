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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.*
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
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.dev.sirasa.screens.admin.data.booking.AddBookingScreen
import com.dev.sirasa.screens.admin.data.booking.DataBookingScreen
import com.dev.sirasa.screens.admin.data.room.AddRoomScreen
import com.dev.sirasa.screens.admin.data.room.DataRoomScreen
import com.dev.sirasa.screens.admin.data.room.EditRoomScreen
import com.dev.sirasa.screens.admin.data.user.AddUserScreen
import com.dev.sirasa.screens.admin.data.user.DataUserScreen
import com.dev.sirasa.screens.admin.data.user.DetailUserScreen
import com.dev.sirasa.screens.admin.qr_code_booking.QrCodeScannerScreen
import com.dev.sirasa.screens.common.contact.ContactUsScreen
import com.dev.sirasa.screens.common.faq.FaqScreen
import com.dev.sirasa.screens.user.history.MoreBookingScreen
import com.dev.sirasa.screens.user.history.MoreHistoryScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    val viewModel: MainViewModel by viewModels()
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
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        setContent {
            viewModel.checkUserSession()
            val authState by viewModel.authState.collectAsState()
            val userRole by viewModel.userRole.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }
            Log.d("role user in mainactivy : ", "${userRole}")
            SirasaTheme(darkTheme = false) {
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
                                composable("main_screen_admin") { MainScreenAdmin(snackbarHostState, userRole) }
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
    val userRole by viewModel.userRole.collectAsState()
    Log.d("role user in authscreen : ", "${userRole}")
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
                enterTransition = { minimalistEnterTransition() },
                exitTransition = { minimalistExitTransition() },
                popEnterTransition = { minimalistPopEnterTransition() },
                popExitTransition = { minimalistPopExitTransition() }
            ) {
                composable("login") { LoginScreen(navController, snackbarHostState) }
                composable("register") { RegisterScreen(navController, snackbarHostState) }
                composable("forget_password") { ResetPasswordScreen(navController,snackbarHostState) }
                composable("verified_account") { VerifiedAccountScreen(snackbarHostState) }
                composable("main_screen_user") { MainScreenUser(snackbarHostState) }
                composable("main_screen_admin") { MainScreenAdmin(snackbarHostState, userRole) }
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
    val routesWithoutBottomBar = listOf(
        RoutesUser.AuthScreen,
        RoutesUser.MoreHistory,
        RoutesUser.MoreBooking,
        RoutesUser.FaqScreen,
        RoutesUser.ContactScreen
    )
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
            modifier = Modifier.consumeWindowInsets(innerPadding),
            enterTransition = { minimalistEnterTransition() },
            exitTransition = { minimalistExitTransition() },
            popEnterTransition = { minimalistPopEnterTransition() },
            popExitTransition = { minimalistPopExitTransition() }
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
            composable(RoutesUser.MoreHistory) { MoreHistoryScreen(onBack = { navController.popBackStack() }) }
            composable(RoutesUser.MoreBooking) { MoreBookingScreen(onBack = { navController.popBackStack() }) }
            composable(RoutesUser.AuthScreen) { AuthScreen(snackbarHostState) }
            composable(RoutesUser.FaqScreen) { FaqScreen(snackbarHostState, onBack = { navController.popBackStack() })}
            composable(RoutesUser.ContactScreen) { ContactUsScreen(onBack = { navController.popBackStack() }) }
        }
    }
}
object RoutesUser {
    const val MoreHistory = "more_history"
    const val MoreBooking = "more_booking"
    const val AuthScreen = "auth_screen"
    const val FaqScreen = "faq_screen"
    const val ContactScreen = "contact_screen"
}

@Composable
fun MainScreenAdmin(snackbarHostState: SnackbarHostState, userRole: String?) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val routesWithoutBottomBar = listOf(
        RoutesAdmin.DataUsers,
        RoutesAdmin.AddUser,
        RoutesAdmin.ProfileDetail,
        RoutesAdmin.DataBookings,
        RoutesAdmin.AddBooking,
        RoutesAdmin.DashboardBooking,
        RoutesAdmin.DataRooms,
        RoutesAdmin.AddRoom,
        RoutesAdmin.EditRoom,
        RoutesAdmin.FaqScreen,
        RoutesAdmin.ContactScreen
    )
    val showBottomBar = currentRoute !in routesWithoutBottomBar

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                BottomNavAdmin(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItemAdmin.Home.route,
            modifier = Modifier.consumeWindowInsets(innerPadding),
            enterTransition = { minimalistEnterTransition() },
            exitTransition = { minimalistExitTransition() },
            popEnterTransition = { minimalistPopEnterTransition() },
            popExitTransition = { minimalistPopExitTransition() }
        ) {
            composable(BottomNavItemAdmin.Home.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    DashboardScreen(navController)
                }
            }
            composable(BottomNavItemAdmin.Room.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    UserRoomScreen()
                }
            }
            composable(BottomNavItemAdmin.ScanQr.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    QrCodeScannerScreen(navController)
                }
            }
            composable(BottomNavItemAdmin.Data.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    DataScreen(navController)
                }
            }
            composable(BottomNavItemAdmin.Profile.route) {
                Box(
                    modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
                ) {
                    ProfileScreen(navController, snackbarHostState)
                }
            }

            composable(RoutesAdmin.AuthScreen) { AuthScreen(snackbarHostState) }
            composable(RoutesAdmin.DataUsers) {
                DataUserScreen(navController, snackbarHostState, userRole) { navController.popBackStack() }
            }
            composable(RoutesAdmin.AddUser) {
                AddUserScreen(navController, snackbarHostState, userRole, onBack = { navController.popBackStack() })
            }
            composable(
                RoutesAdmin.ProfileDetail,
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                userId?.let {
                    DetailUserScreen(navController,snackbarHostState, userRole,userId, onBack = { navController.popBackStack() })
                }
            }
            composable(RoutesAdmin.DataBookings) {
                DataBookingScreen(navController, snackbarHostState, onBack = {navController.popBackStack()})
            }
            composable(RoutesAdmin.AddBooking) {
                AddBookingScreen(navController, snackbarHostState, { navController.popBackStack() })
            }
            composable(
                RoutesAdmin.DashboardBooking,
                arguments = listOf(
                    navArgument("startDate") { type = NavType.StringType },
                    navArgument("endDate") { type = NavType.StringType },
                    navArgument("status") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val startDate = backStackEntry.arguments?.getString("startDate") ?: ""
                val endDate = backStackEntry.arguments?.getString("endDate") ?: ""
                val status = backStackEntry.arguments?.getString("status") ?: "all"

                DataBookingScreen(
                    navController = navController,
                    snackbarHostState = remember { SnackbarHostState() },
                    onBack = { navController.popBackStack() },
                    startDate = startDate,
                    endDate = endDate,
                    status = status
                )
            }
            composable(RoutesAdmin.DataRooms) {
                DataRoomScreen(navController, snackbarHostState, userRole,  onBack = { navController.popBackStack() })
            }
            composable(RoutesAdmin.AddRoom) {
                AddRoomScreen(navController, snackbarHostState, userRole, onBack = { navController.popBackStack() })
            }
            composable(
                RoutesAdmin.EditRoom,
                arguments = listOf(navArgument("roomId") { type = NavType.StringType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
                EditRoomScreen(navController, snackbarHostState , userRole, roomId, onBack = { navController.popBackStack() })
            }
            composable(RoutesAdmin.FaqScreen) { FaqScreen(snackbarHostState, onBack = { navController.popBackStack() })}
            composable(RoutesAdmin.ContactScreen) { ContactUsScreen(onBack = { navController.popBackStack() }) }
        }
    }
}

object RoutesAdmin {
    const val AuthScreen = "auth_screen"
    const val DataUsers = "data_users"
    const val AddUser = "add_user"
    const val ProfileDetail = "profile/{userId}"
    const val DataBookings = "data_bookings"
    const val AddBooking = "add_booking"
    const val DashboardBooking = "dataBooking/{startDate}/{endDate}/{status}"
    const val DataRooms = "data_rooms"
    const val AddRoom = "add_room"
    const val EditRoom = "edit_room/{roomId}"
    const val FaqScreen = "faq_screen"
    const val ContactScreen = "contact_screen"
}

// Improved NavHost animations
fun minimalistEnterTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { width -> width },
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(300))
}

fun minimalistExitTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { width -> -width },
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(300))
}

fun minimalistPopEnterTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { width -> -width },
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(300))
}

fun minimalistPopExitTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { width -> width },
        animationSpec = tween(300, easing = LinearOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(300))
}