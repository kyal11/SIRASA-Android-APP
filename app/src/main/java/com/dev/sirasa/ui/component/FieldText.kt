package com.dev.sirasa.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun FieldText(
    modifier: Modifier = Modifier,
    title: String,
    value: MutableState<String>,
    hint: String,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = value.value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            placeholder = { Text(hint) },
            leadingIcon = leadingIcon?.let { { Icon(imageVector = it, contentDescription = null) } },
            trailingIcon = trailingIcon?.let { { Icon(imageVector = it, contentDescription = null) } },
            shape = RoundedCornerShape(8.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default
            ),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password) else KeyboardOptions.Default,
            maxLines = 5
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFieldText() {
    val textState = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    FieldText(
        title = "Email",
        value = textState,
        hint = "Masukkan email Anda",
        onValueChange = { textState.value = it }
    )
}
