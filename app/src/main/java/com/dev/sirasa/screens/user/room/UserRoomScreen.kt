package com.dev.sirasa.screens.user.room

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.ui.theme.Green300
import com.dev.sirasa.ui.theme.Green900
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun UserRoomScreen() {
    var selectedOption by remember { mutableIntStateOf(0) }
    val timeSlots = listOf(
        "08.00" to false,
        "09.00" to false,
        "10.00" to true,
        "11.00" to false,
        "13.00" to false,
        "14.00" to true,
        "15.00" to false,
        "16.00" to false,
        "17.00" to false,
        "18.00" to false
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Informasi Ruangan",
            style = Typography.displayLarge
        )
        TabDayRoom(
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it }
        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                CardRoom("Ruang Merpati", "2", "5", "27 Desember 2025", timeSlots)
                CardRoom("Ruang Merpati", "2", "5", "27 Desember 2025", timeSlots)
                CardRoom("Ruang Merpati", "2", "5", "27 Desember 2025", timeSlots)
            }
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardRoom(roomName: String, floorName: String, capacitu: String, dateRoom: String,  timeSlots: List<Pair<String, Boolean>>) {
    Card (
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Column (
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    roomName,
                    style = Typography.titleLarge,
                    color = Green900
                )

                Text(
                    "Lantai ${floorName}",
                    style = Typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Kapasitas ${capacitu} orang",
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Keterangan Waktu :",
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                dateRoom,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Badge(
                        containerColor = Green300,
                        modifier = Modifier.width(16.dp).height(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Belum dipesan",
                        style = Typography.bodyMedium
                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Badge(
                        containerColor = Color.Red,
                        modifier = Modifier.width(16.dp).height(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Sudah Dipesan",
                        style = Typography.bodyMedium
                    )
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                timeSlots.forEach { (time, isBooked) ->
                    SlotTime(time, isBooked)
                }
            }
        }
    }
}


@Composable
fun SlotTime(time: String, isBooked: Boolean) {
    Badge(
        containerColor = if (isBooked) Color.Red else Green300,
        contentColor = if (isBooked) Color.White else Color.Black,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            time,
            style = Typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun TabDayRoom(
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    listOption: List<String> = listOf("27 Feb", "28 Feb", "29 Feb")
) {
    TabRow(
        selectedTabIndex = selectedOption,
        containerColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .clip(RoundedCornerShape(50))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(50)),
        indicator = {},
    ) {
        listOption.forEachIndexed { index, text ->
            Tab(
                selected = selectedOption == index,
                onClick = { onOptionSelected(index) },
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (selectedOption == index) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = when (index) {
                            0 -> RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp)
                            listOption.size - 1 -> RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp)
                            else -> RoundedCornerShape(0.dp)
                        }
                    )
            ) {
                Text(
                    text = text,
                    color = if (selectedOption == index) Color.White else Color.Black,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                )
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PreviewTabRoom() {
//    var selectedOption by remember { mutableIntStateOf(0) }
//    SirasaTheme {
//        TabDayRoom(
//            selectedOption = selectedOption,
//            onOptionSelected = { selectedOption = it }
//        )
//    }
//}
//@Preview(showBackground = true)
//@Composable
//fun PreviewCard() {
//    val timeSlots = listOf(
//        "08.00" to false,
//        "09.00" to false,
//        "10.00" to true,
//        "11.00" to false,
//        "13.00" to false,
//        "14.00" to true,
//        "15.00" to false,
//        "16.00" to false,
//        "17.00" to false,
//        "18.00" to false
//    )
//    SirasaTheme {
//        CardRoom("Ruang Merpati", "2", "5", "27 Desember 2025", timeSlots)
//    }
//}
@Preview(showBackground = true)
@Composable
fun PreviewUserRoom() {
    SirasaTheme {
        UserRoomScreen()
    }
}