package com.dev.sirasa.screens.admin.data

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.dev.sirasa.R
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun DataUserScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("All") }
    val roles = listOf("All","Super Admin", "Admin", "User")
    val users = listOf(
        UserData("John Doe", "12345678", "08123456789", R.drawable.perpus_logo),
        UserData("Jane Smith", "87654321", "08987654321", R.drawable.perpus_logo),
        UserData("Alice Johnson", "11223344", "08567891234", R.drawable.perpus_logo)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        InputFieldSearch(
            label = "Search",
            placeHolder = "Enter name",
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth()
        )

        // Role Filter
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            roles.forEach { role ->
                Button(
                    onClick = { selectedRole = role },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRole == role) MaterialTheme.colorScheme.primary else Color.White ,
                        contentColor = if (selectedRole == role) Color.White else Color.Black
                    ),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Text(text = role)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // User List
        users.forEach { user ->
            CardUser(user)
        }
    }
}

@Composable
fun InputFieldSearch(label: String, placeHolder: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text, modifier: Modifier = Modifier) {
    OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = { Text(text = placeHolder, style = Typography.bodyMedium, color = Color.Gray) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE2E8F0),
            ),
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon")
            }
        )
    Spacer(modifier = Modifier.height(8.dp))
}
@Composable
fun CardUser(user: UserData) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = user.imageRes), contentDescription = "Profile Image", modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, style = Typography.titleMedium)
                Text(text = "NIM: ${user.nim}", style = Typography.bodyMedium)
                Text(text = "Phone: ${user.phone}", style = Typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDataUserScreen() {
    SirasaTheme{
        DataUserScreen()
    }
}

data class UserData(val name: String, val nim: String, val phone: String, val imageRes: Int)
