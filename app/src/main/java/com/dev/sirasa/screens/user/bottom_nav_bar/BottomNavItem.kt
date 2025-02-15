package com.dev.sirasa.screens.user.bottom_nav_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.dev.sirasa.R

sealed class BottomNavItem(val route: String, val icon: Int, val label: String){
    object Home: BottomNavItem("home", R.drawable.home_icon, "Home")
    object Room: BottomNavItem("room", R.drawable.meating_room_icon, "Room")
    object History: BottomNavItem("history", R.drawable.history_icon, "History")
    object Profile: BottomNavItem("profile", R.drawable.profile_icon, "Profile")
}