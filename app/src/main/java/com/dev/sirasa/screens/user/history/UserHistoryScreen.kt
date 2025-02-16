package com.dev.sirasa.screens.user.history

import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.ui.theme.Black
import com.dev.sirasa.ui.theme.Green300
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography
import com.dev.sirasa.ui.theme.White

@Composable
fun UserHistoryScreen() {

}

@Composable
fun CardHistory() {
    Card (
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("10 Maret 2025", color = MaterialTheme.colorScheme.onPrimaryContainer)
                IconButton( onClick = {}) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More", tint =  MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Rizky Al Arief",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("083812721450", style = Typography.bodyMedium)
                }
                Badge(
                    modifier = Modifier.padding(8.dp),
                    containerColor = Green300,
                    contentColor = Black
                ) {
                    Text(
                        "Konfirmasi",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = Typography.titleMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text("10:00 - 12:00", style = Typography.bodyMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Text("Ruang Diskusi Merpati", style = Typography.bodyMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Text("10 Orang", style = Typography.bodyMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Sesi Brainstorming untuk pengembangan strategi pemasaran produk baru.",
                style = Typography.bodyMedium
            )

            Button(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black
                )
            ) {
                Text("Show Qr Code", style = Typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardHistory() {
    SirasaTheme {
        CardHistory()
    }
}