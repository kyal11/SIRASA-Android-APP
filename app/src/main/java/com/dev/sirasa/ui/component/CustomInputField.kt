package com.dev.sirasa.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dev.sirasa.ui.theme.Typography

@Composable
fun InputField(label: String? = null ,placeHolder: String, value: String, onValueChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text, maxLines: Int? = null ,modifier: Modifier = Modifier) {
    val filteredValue = remember(value) {
        if (maxLines == 1) value.replace("\n", "") else value
    }
    Column(modifier = modifier.fillMaxWidth()) {
       if (label != null) {
           Text(text = label, style = Typography.displayMedium, modifier = Modifier.padding(bottom = 8.dp))
       }
        OutlinedTextField(
            value = value,
            maxLines = maxLines ?: 3,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = { Text(text = "$placeHolder", style = Typography.bodyMedium, color = Color.Gray) },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = if (maxLines == 1) ImeAction.Done else ImeAction.Default
            ),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE2E8F0),
            ),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun InputFieldTextArea(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 4,
    maxLines: Int = 6,
    modifier: Modifier = Modifier
) {
    Column(modifier =modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = Typography.displayMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .height((minLines * 24).dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            placeholder = {
                Text(
                    text = "Masukkan $label",
                    style = Typography.bodyMedium,
                    color = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE2E8F0),
            ),
            minLines = minLines,
            maxLines = maxLines
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}