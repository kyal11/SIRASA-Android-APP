package com.dev.sirasa.screens.admin.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.sirasa.screens.user.room.TabDayRoom
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(navController: NavController, viewModel: DashboardViewModel = hiltViewModel()) {
    var selectedOption by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Hari Ini", "Besok", "Lusa")
    val dashboardState by viewModel.dashboardState.collectAsState()

    val currentDate = remember {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        sdf.format(Date())
    }

    LaunchedEffect(selectedOption) {
        val dayFilter = when (selectedOption) {
            0 -> "1"
            1 -> "2"
            2 -> "3"
            else -> "1"
        }
        viewModel.getSummaryBooking(dayFilter)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            "Selamat Datang, Admin",
            style = Typography.titleLarge
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            currentDate,
            style = Typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        TabDayRoom(
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it },
            tabTitles
        )

        when (dashboardState) {
            is DashboardState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoadingCircular(true, modifier = Modifier.align(Alignment.Center))
                }
            }
            is DashboardState.Success -> {
                val data = (dashboardState as DashboardState.Success).data
                CardDashboard("Total Ruangan", data.data?.totalRooms.toString(), listOf(Color(0xFF3DD043), Color(0xFF3F51B5)))
                CardDashboard("Total Peminjaman", data.data?.totalBookings.toString(), listOf(Color(0xFF00BCD4), Color(0xFF8BC34A)))
                CardDashboard("Total Peminjaman Dipesan", data.data?.bookedBookings.toString(), listOf(Color(0xFF36D1DC), Color(0xFF5B86E5)))
                CardDashboard("Total Peminjaman Selesai", data.data?.doneBookings.toString(), listOf(Color(0xFFFF9800), Color(0xFF8BC34A)))
                CardDashboard("Total Peminjaman Dibatalkan", data.data?.canceledBookings.toString(), listOf(Color(0xFFFF512F), Color(0xFFDD2476)))
            }
            is DashboardState.Error -> {
                Text("Error: \${(dashboardState as DashboardState.Error).message}")
            }
            else -> {}
        }
    }
}
@Composable
fun CardDashboard(
    title: String,
    value: String,
    gradientColors: List<Color>,
    onDetailClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Brush.horizontalGradient(gradientColors))
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = Typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f) // Membuat title memenuhi ruang yang tersedia
                    )

                    // Menampilkan IconButton jika onDetailClick tidak null
                    if (onDetailClick != null) {
                        IconButton(
                            onClick = onDetailClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "More",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = value,
                    style = Typography.titleLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}



@Composable
fun IconButton(onClick: Unit, modifier: Unit, content: Unit) {

}

//@Preview(showBackground = true)
//@Composable
//fun PreviewCardDashboard() {
//    SirasaTheme {
//        CardDashboard("")
//    }
//}
//@Preview(showBackground = true)
//@Composable
//fun PreviewDashboard() {
//    SirasaTheme {
//        DashboardScreen()
//    }
//}