package com.dev.sirasa.screens.user.room

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun UserRoomScreen() {

}
@Composable
fun CardRoom() {
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCard() {

}
@Preview(showBackground = true)
@Composable
fun PreviewUserRoom() {
}