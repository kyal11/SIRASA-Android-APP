package com.dev.sirasa.screens.admin.data

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.ui.theme.Green300
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun DataScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Data SIRASA",
            style = Typography.displayLarge
        )
        Text(
            "Content Management System SIRASA",
            style = Typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        CardDataUser(
            title = "Data Pengguna",
            total = "100",
            userData = listOf(
                "Super Admin" to "10",
                "Admin" to "10",
                "Pengguna" to "10"
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        CardDataRoom(
            title = "Data Ruangan",
            total = "7",
            roomData = listOf(
                Triple("Ruang Merpati", "2", "08:00-17:00"),
                Triple("Ruang Kenari", "4", "09:00-18:00"),
                Triple("Ruang Garuda", "6", "07:00-16:00"),
                Triple("Ruang Cendrawasih", "3", "10:00-19:00"),
                Triple("Ruang Elang", "5", "08:30-17:30"),
                Triple("Ruang Rajawali", "8", "07:30-16:30"),
                Triple("Ruang Kutilang", "2", "09:30-18:30")
            )
        )
    }
}

@Composable
fun CardDataUser(title: String, total: String, userData: List<Pair<String, String>>) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $total",
                style = Typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            userData.forEachIndexed { index, data ->
                UserDataRow(role = data.first, count = data.second)
                if (index < userData.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun UserDataRow(role: String, count: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = role, style = Typography.bodyMedium)
        Text(text = count, style = Typography.bodyMedium)
    }
}

@Composable
fun CardDataRoom(title: String, total: String, roomData: List<Triple<String, String, String>>) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $total",
                style = Typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Name", style = Typography.labelLarge, modifier = Modifier.weight(2f))
                Text(text = "Capacity", style = Typography.labelLarge, modifier = Modifier.weight(1f))
                Text(text = "Time", style = Typography.labelLarge, modifier = Modifier.weight(2f), textAlign = TextAlign.End)
            }
            Divider()

            roomData.forEachIndexed { index, data ->
                RoomDataRow(name = data.first, capacity = data.second, time = data.third)
                if (index < roomData.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun RoomDataRow(name: String, capacity: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text(text = name, style = Typography.bodyMedium, modifier = Modifier.weight(2f))
        Text(text = capacity, style = Typography.bodyMedium, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        Text(text = time, style = Typography.bodyMedium, modifier = Modifier.weight(2f), textAlign = TextAlign.End)
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewCardScreen() {
    SirasaTheme {
        CardDataUser(
            title = "Data Pengguna",
            total = "100",
            userData = listOf(
                "Super Admin" to "10",
                "Admin" to "10",
                "Pengguna" to "10"
            )
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewDataScreen() {
    SirasaTheme {
        DataScreen()
    }
}