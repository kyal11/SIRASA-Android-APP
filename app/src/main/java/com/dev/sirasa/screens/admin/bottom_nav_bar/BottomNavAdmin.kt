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

@Composable
fun BottomNavAdmin(navController: NavController) {
    val items = listOf(
        BottomNavItemAdmin.Home,
        BottomNavItemAdmin.Room,
        BottomNavItemAdmin.ScanQr,
        BottomNavItemAdmin.Data,
        BottomNavItemAdmin.Profile
    )
    val currentRoute = navController.currentBackStackEntryAsState()
        .value?.destination?.route

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = Color.White,
            modifier = Modifier.clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        ) {
            items.forEach { item ->
                if (item == BottomNavItemAdmin.ScanQr) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
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
        }
        FloatingActionButton(
            onClick = {
                if (currentRoute != BottomNavItemAdmin.ScanQr.route) {
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
            containerColor = MaterialTheme.colorScheme.primary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 3.dp,
                focusedElevation = 3.dp,
                hoveredElevation = 3.dp
            )
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

@Preview(showBackground = true)
@Composable
fun preview() {
    val navController = rememberNavController()
    SirasaTheme {
        BottomNavAdmin(navController)
    }
}
