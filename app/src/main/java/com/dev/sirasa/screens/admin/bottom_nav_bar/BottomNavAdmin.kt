package com.dev.sirasa.screens.admin.bottom_nav_bar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@Composable
fun BottomNavAdmin(navController: NavController) {
    val items = listOf(
        BottomNavItemAdmin.Home,
        BottomNavItemAdmin.Room,
        BottomNavItemAdmin.ScanQr,
        BottomNavItemAdmin.Data,
        BottomNavItemAdmin.Profile
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var previousRoute by remember { mutableStateOf<String?>(null) }
    var isLocked by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentRoute) {
        if (previousRoute == BottomNavItemAdmin.ScanQr.route &&
            currentRoute != BottomNavItemAdmin.ScanQr.route) {
            isLocked = true
            coroutineScope.launch {
                kotlinx.coroutines.delay(1000)
                isLocked = false
            }
        }
        previousRoute = currentRoute
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
        ) {
            items.take(2).forEach { item ->
                NavigationBarItem(
                    icon = { Icon(painterResource(id = item.icon), contentDescription = item.label) },
                    label = { Text(item.label, style = Typography.bodyMedium) },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Spacer(modifier = Modifier.width(72.dp))

            items.takeLast(2).forEach { item ->
                NavigationBarItem(
                    icon = { Icon(painterResource(id = item.icon), contentDescription = item.label) },
                    label = { Text(item.label, style = Typography.bodyMedium) },
                    selected = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        // FAB untuk Scan QR
        FloatingActionButton(
            onClick = {
                if (!isLocked && currentRoute != BottomNavItemAdmin.ScanQr.route) {
                    navController.navigate(BottomNavItemAdmin.ScanQr.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp),
            shape = CircleShape,
            containerColor = if (isLocked) Color.Gray else MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 3.dp,
                focusedElevation = 3.dp,
                hoveredElevation = 3.dp
            ),
        ) {
            Icon(
                painter = painterResource(id = BottomNavItemAdmin.ScanQr.icon),
                contentDescription = BottomNavItemAdmin.ScanQr.label,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
// Preview untuk berbagai device
@Preview(name = "Phone (Small)", widthDp = 320, heightDp = 600, showBackground = true)
@Composable
fun PreviewBottomNavSmallPhone() {
    val navController = rememberNavController()
    SirasaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BottomNavAdmin(navController = navController)
        }
    }
}

@Preview(name = "Phone (Medium)", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun PreviewBottomNavMediumPhone() {
    val navController = rememberNavController()
    SirasaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BottomNavAdmin(navController = navController)
        }
    }
}

@Preview(name = "Phone (Large)", widthDp = 420, heightDp = 900, showBackground = true)
@Composable
fun PreviewBottomNavLargePhone() {
    val navController = rememberNavController()
    SirasaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BottomNavAdmin(navController = navController)
        }
    }
}

@Preview(name = "Tablet", widthDp = 768, heightDp = 1024, showBackground = true)
@Composable
fun PreviewBottomNavTablet() {
    val navController = rememberNavController()
    SirasaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BottomNavAdmin(navController = navController)
        }
    }
}

@Preview(name = "Landscape Mode", widthDp = 800, heightDp = 360, showBackground = true)
@Composable
fun PreviewBottomNavLandscape() {
    val navController = rememberNavController()
    SirasaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BottomNavAdmin(navController = navController)
        }
    }
}

// Untuk device dengan ukuran besar (fold)
@Preview(name = "Foldable", widthDp = 840, heightDp = 900, showBackground = true)
@Composable
fun PreviewBottomNavFoldable() {
    val navController = rememberNavController()
    SirasaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BottomNavAdmin(navController = navController)
        }
    }
}

@Composable
fun PreviewBottomNavDarkMode() {
    val navController = rememberNavController()
    SirasaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            BottomNavAdmin(navController = navController)
        }
    }
}

