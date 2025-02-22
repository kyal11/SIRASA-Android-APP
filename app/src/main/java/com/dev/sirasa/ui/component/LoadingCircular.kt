package com.dev.sirasa.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dev.sirasa.ui.theme.SirasaTheme

@Composable
fun LoadingCircular(loading: Boolean) {
    if (!loading) return

    CircularProgressIndicator(
        modifier = Modifier.width(64.dp).height(64.dp),
        color = MaterialTheme.colorScheme.onPrimary,
        trackColor = MaterialTheme.colorScheme.primary
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoading() {
    SirasaTheme {
        LoadingCircular(true)
    }
}