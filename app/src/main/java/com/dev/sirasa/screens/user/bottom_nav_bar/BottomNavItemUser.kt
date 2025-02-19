package com.dev.sirasa.screens.user.bottom_nav_bar

import com.dev.sirasa.R

sealed class BottomNavItemUser(val route: String, val icon: Int, val label: String){
    object Home: BottomNavItemUser("home", R.drawable.home_icon, "Home")
    object Room: BottomNavItemUser("room", R.drawable.meating_room_icon, "Room")
    object History: BottomNavItemUser("history", R.drawable.history_icon, "History")
    object Profile: BottomNavItemUser("profile", R.drawable.profile_icon, "Profile")
}