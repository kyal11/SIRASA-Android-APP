package com.dev.sirasa.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun CustomOptionTime(
    options: List<String>,
    selectedOptions: List<String>,
    onOptionSelected: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf(selectedOptions.toMutableList()) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Slot Waktu",
            style = Typography.displayMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = if (selectedItems.isEmpty()) "Pilih Slot Waktu"
                else selectedItems.sortedBy { options.indexOf(it) }.joinToString(", "),
            onValueChange = {},
            readOnly = true,
            textStyle = if (selectedItems.isEmpty())  MaterialTheme.typography.bodyMedium
                else MaterialTheme.typography.bodyMedium.copy(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable { expanded = true }
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = {
                Text("Pilih Slot Waktu", style = Typography.bodyMedium, color = Color.Gray)
            },
            enabled = false,
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
                            onOptionSelected(selectedItems)
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
                        options.forEachIndexed { index, option ->
                            val isDisabled = isOptionDisabled(options, selectedItems, index)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = option,
                                    style = Typography.bodyMedium,
                                    color = if (isDisabled) Color.Gray else Color.Black
                                )
                                Checkbox(
                                    checked = selectedItems.contains(option),
                                    enabled = !isDisabled,
                                    onCheckedChange = { isChecked ->
                                        selectedItems = selectedItems.toMutableList().apply {
                                            if (isChecked) {
                                                if (size < 2) add(option)
                                            } else {
                                                remove(option)
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
private fun isOptionDisabled(options: List<String>, selectedItems: List<String>, index: Int): Boolean {
    if (selectedItems.isEmpty()) return false
    if (selectedItems.contains(options[index])) return false

    if (selectedItems.size >= 2) return true

    val selectedIndex = options.indexOf(selectedItems.first())
    val isAdjacent = index == selectedIndex - 1 || index == selectedIndex + 1
    return !isAdjacent
}
@Preview(showBackground = true)
@Composable
fun PreviewCustomOptionTime() {
    var selectedSlots by remember { mutableStateOf<List<String>>(emptyList()) }

    SirasaTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            CustomOptionTime(
                options = listOf("08:00-09:00", "09:00-10:00", "10:00-11:00"),
                selectedOptions = selectedSlots,
                onOptionSelected = { selectedSlots = it }
            )
        }
    }
}
