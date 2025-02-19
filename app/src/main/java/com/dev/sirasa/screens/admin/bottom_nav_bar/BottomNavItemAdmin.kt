package com.dev.sirasa.screens.admin.bottom_nav_bar

import com.dev.sirasa.R

sealed class BottomNavItemAdmin(val route: String, val icon: Int, val label: String) {
    data object Home: BottomNavItemAdmin("home", R.drawable.home_icon, "Home")
    data object Room: BottomNavItemAdmin("room", R.drawable.meating_room_icon, "Room")
    data object ScanQr: BottomNavItemAdmin("scan", R.drawable.qr_icon, "ScanQr")
    data object Data: BottomNavItemAdmin("data", R.drawable.data_icon, "Data")
    data object Profile: BottomNavItemAdmin("profile", R.drawable.profile_icon, "Profile")
}