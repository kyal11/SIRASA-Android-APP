package com.dev.sirasa.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun CustomOptionTime(
    enabledField: Boolean,
    options: List<Pair<String, String?>>, // Menggunakan Pair("startTime-endTime", id)
    selectedOptions: List<String>, // Menyimpan hanya ID slot
    onOptionSelected: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf(selectedOptions.toMutableList()) }

    LaunchedEffect(options) {
        // Reset selectedItems ketika options berubah
        Log.d("CustomOptionTime", "🔍 Available Slots: ${options.map { it.first }}")
        Log.d("CustomOptionTime", "🔍 Available Slot IDs: ${options.map { it.second }}")
        Log.d("CustomOptionTime", "🔍 Initial Selected Slot IDs: $selectedOptions")
        selectedItems = mutableListOf()
        onOptionSelected(emptyList())
        Log.d("CustomOptionTime2", "🔍 Available Slots: ${options.map { it.first }}")
        Log.d("CustomOptionTime2", "🔍 Available Slot IDs: ${options.map { it.second }}")
        Log.d("CustomOptionTime2", "🔍 Initial Selected Slot IDs: $selectedOptions")
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Slot Waktu",
            style = Typography.displayMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = selectedItems
                .mapNotNull { id -> options.find { it.second == id } }
                .sortedBy {
                    it.first.split("-")[0].trim()
                }
                .joinToString(", ") { it.first },
            onValueChange = {},
            readOnly = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable { expanded = true }
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = {
                Text("Pilih Slot Waktu", style = Typography.bodyMedium, color = Color.Gray)
            },
            enabled = enabledField,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE2E8F0),
            ),
            shape = RoundedCornerShape(8.dp),
            maxLines = 2
        )

        if (expanded) {
            AlertDialog(
                onDismissRequest = { expanded = false },
                containerColor = Color.White,
                confirmButton = {
                    TextButton(
                        onClick = {
                            onOptionSelected(selectedItems.toList())
                            expanded = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { expanded = false }) {
                        Text("Batal")
                    }
                },
                title = { Text("Pilih Slot Waktu", textAlign = TextAlign.Center) },
                text = {
                    Column {
                        options.forEachIndexed { index, (time, id) -> // Perbaiki forEachIndexed agar mendapatkan index
                            val isDisabled = isOptionDisabled(options, selectedItems, index)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = time,
                                    style = Typography.bodyMedium,
                                    color = if (isDisabled) Color.Gray else Color.Black,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Checkbox(
                                    checked = selectedItems.contains(id),
                                    enabled = !isDisabled,
                                    onCheckedChange = { isChecked ->
                                        selectedItems = selectedItems.toMutableList().apply {
                                            if (isChecked) {
                                                if (size < 2) id?.let { add(it) }
                                            } else {
                                                remove(id)
                                            }
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

private fun isOptionDisabled(
    options: List<Pair<String, String?>>, // Menggunakan Pair("startTime-endTime", id)
    selectedItems: List<String>, // List ID yang dipilih
    index: Int
): Boolean {
    if (selectedItems.isEmpty()) return false
    if (selectedItems.contains(options[index].second)) return false // Cek berdasarkan ID

    if (selectedItems.size >= 2) return true // Batasi pilihan maksimal 2 slot

    // Ambil indeks dari slot yang pertama dipilih berdasarkan ID
    val selectedIndex = options.indexOfFirst { it.second == selectedItems.firstOrNull() }
    if (selectedIndex == -1) return false // Jika tidak ada slot yang cocok, tidak disable

    val isAdjacent = index == selectedIndex - 1 || index == selectedIndex + 1
    return !isAdjacent
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewCustomOptionTime() {
//    var selectedSlots by remember { mutableStateOf<List<String>>(emptyList()) }
//
//    SirasaTheme {
//        Column(modifier = Modifier.padding(16.dp)) {
//            CustomOptionTime(
//                true,
//                options = listOf("08:00-09:00", "09:00-10:00", "10:00-11:00"),
//                selectedOptions = selectedSlots,
//                onOptionSelected = { selectedSlots = it }
//            )
//        }
//    }
//}
