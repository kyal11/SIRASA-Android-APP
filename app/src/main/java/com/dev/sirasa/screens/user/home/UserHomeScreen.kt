package com.dev.sirasa.screens.user.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.R
import com.dev.sirasa.ui.component.CustomOptionTime
import com.dev.sirasa.ui.component.DateField
import com.dev.sirasa.ui.component.DropdownField
import com.dev.sirasa.ui.component.InputField
import com.dev.sirasa.ui.component.InputFieldTextArea
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography

@Composable
fun UserHomeScreen() {
    var description by remember { mutableStateOf("") }
    var roomOption by remember { mutableStateOf("") }
    var selectedSlots by remember { mutableStateOf<List<String>>(emptyList()) }
    var capacity by remember { mutableStateOf("") }
    Column (
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp, horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.perpus_logo),
            contentDescription = "Logo Sirasa",
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp).height(80.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Selamat Datang di SIRASA",
            style = Typography.displayLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Silakan isi formulir di bawah ini untuk meminjam ruang diskusi kami.",
            style = Typography.displayMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        DateField()
        Spacer(modifier = Modifier.height(12.dp))

        DropdownField(
            options = listOf("Ruangan 1", "Ruangan 2", "Ruangan 3"),
            selectedOption = roomOption,
            onOptionSelected = { roomOption = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomOptionTime(
                options = listOf("08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00",
                    "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00"),
                selectedOptions = selectedSlots,
                onOptionSelected = { selectedSlots = it },
                modifier = Modifier.weight(1f)
            )

            InputField(
                label = "Jumlah Peserta",
                placeHolder = "Peserta",
                value = capacity,
                onValueChange = { capacity = it },
                keyboardType = KeyboardType.Number,
                modifier = Modifier.width(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        InputFieldTextArea("Keterangan Keperluan", description, {description = it}, KeyboardType.Text)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Red)) { append("* ") }
                append("Harap diperhatikan: Jika Anda tidak tiba di resepsionis dalam 10 menit setelah waktu mulai yang Anda pilih, reservasi akan otomatis dibatalkan.")
            },
            style = Typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { /* TODO: Implementasi Register */ },
            modifier = Modifier.fillMaxWidth().height(48.dp),

            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Submit", style = Typography.titleMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserHome() {
    SirasaTheme {
        UserHomeScreen()
    }
}