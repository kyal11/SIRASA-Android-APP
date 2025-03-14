package com.dev.sirasa.screens.common.email_verification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dev.sirasa.R
import com.dev.sirasa.ui.theme.SirasaTheme
import com.dev.sirasa.ui.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun VerifiedAccountScreen(
    snackbarHostState: SnackbarHostState,
    viewModel: VerifiedViewModel = hiltViewModel(),
) {
    var isButtonEnabled by remember { mutableStateOf(false) }
    var timer by remember { mutableIntStateOf(60) }
    val verifiedState by viewModel.verifiedState.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.email_animation))

    LaunchedEffect(Unit) {
        viewModel.sendEmail()
    }

    LaunchedEffect(timer) {
        if (timer > 0) {
            delay(1000L)
            timer--
        } else {
            isButtonEnabled = true
        }
    }

    LaunchedEffect(verifiedState) {
        when (verifiedState) {
            is VerifiedState.Success -> {
                snackbarHostState.showSnackbar("Email verification sent successfully!")
            }
            is VerifiedState.Error -> {
                snackbarHostState.showSnackbar((verifiedState as VerifiedState.Error).message)
                isButtonEnabled = true
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier.fillMaxWidth().height(250.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )
        Text(text = "Verifikasi akunmu", style = Typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Kami sudah mengirim email kepada kamu, tolong segera konfirmasi...",
            style = Typography.displayMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isButtonEnabled = false
                timer = 60
                viewModel.sendEmail()
            },
            enabled = isButtonEnabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = if (isButtonEnabled) "Resend Email" else "Tunggu $timer detik")
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PreviewVerifiedAccountScreen() {
//    SirasaTheme {
//        VerifiedAccountScreen()
//    }
//}
