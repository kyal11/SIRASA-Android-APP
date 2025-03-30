package com.dev.sirasa.screens.admin.qr_code_booking

import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import android.Manifest;
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.dev.sirasa.R
import com.dev.sirasa.ui.component.LoadingCircular
import com.dev.sirasa.ui.theme.GrayBackground
import com.dev.sirasa.ui.theme.Green300
import com.dev.sirasa.ui.theme.Green600
import com.dev.sirasa.ui.theme.Green700
import com.dev.sirasa.ui.theme.Green800
import com.dev.sirasa.ui.theme.Green900
import com.dev.sirasa.ui.theme.Typography
import com.dev.sirasa.utils.QRCodeAnalyzer
import com.dev.sirasa.utils.formatDate
import com.dev.sirasa.utils.formatTimeSlot
import com.google.mlkit.vision.barcode.BarcodeScanning

@Composable
fun QrCodeScannerScreen(
    navController: NavController,
    viewModel: ScanViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember(context) { ProcessCameraProvider.getInstance(context) }
    val scanState = remember { mutableStateOf<String?>(null) }
    val bookingValidationState = viewModel.bookingValidationState.collectAsState()

    val cameraPermissionGranted = remember { mutableStateOf(false) }
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            cameraPermissionGranted.value = isGranted
        }
    )

    LaunchedEffect(Unit) {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            cameraPermissionGranted.value = true
        }
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            try {
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
            } catch (e: Exception) {
                Log.e("QrCodeScannerScreen", "Error unbinding camera", e)
            }
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        if (cameraPermissionGranted.value) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx).apply {
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                        }

                        cameraProviderFuture.addListener({
                            try {
                                val cameraProvider = cameraProviderFuture.get()
                                cameraProvider.unbindAll()

                                val preview = Preview.Builder().build()
                                preview.setSurfaceProvider(previewView.surfaceProvider)

                                val cameraSelector = CameraSelector.Builder()
                                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                    .build()

                                val imageAnalysis = ImageAnalysis.Builder()
                                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                    .build()

                                imageAnalysis.setAnalyzer(
                                    ContextCompat.getMainExecutor(ctx),
                                    QRCodeAnalyzer { result ->
                                        if (scanState.value == null) {
                                            scanState.value = result
                                            viewModel.validateBooking(result)
                                        }
                                    }
                                )

                                cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview,
                                    imageAnalysis
                                )
                            } catch (e: Exception) {
                                Log.e("QrCodeScannerScreen", "Camera binding failed", e)
                            }
                        }, ContextCompat.getMainExecutor(ctx))

                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(250.dp)
                            .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Izin kamera diperlukan untuk memindai QR code", textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {  cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Berikan Izin Kamera")
                    }
                }
            }
        }

        if (cameraPermissionGranted.value) {
                when (bookingValidationState.value) {
                    is ScanState.Idle -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Arahkan kamera ke QR code",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    is ScanState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxWidth().background(Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingCircular(
                                true,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    is ScanState.Success -> {
                        val data = (bookingValidationState.value as ScanState.Success).data
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .border(
                                    width = 2.dp,
                                    color = GrayBackground
                                )
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val date =
                                formatDate(data.bookingSlot?.firstOrNull()?.slot?.date)
                            val timeSlot = formatTimeSlot(data.bookingSlot)
                            Text(
                                "Booking Valid!",
                                style = MaterialTheme.typography.titleLarge,
                                color = Green700
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.check_circle_icon),
                                    contentDescription = "Success",
                                    modifier = Modifier.size(64.dp)
                                )
//                                Icon(
//                                    imageVector = Icons.Default.Check,
//                                    contentDescription = "Berhasil",
//                                    modifier = Modifier.size(64.dp),
//                                    tint = Green300
//                                )
                            }
                            Text(
                                "Berhasil",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleLarge,
                                color = Green700
                            )
                            Column {
                                Text(date, textAlign = TextAlign.End, style = MaterialTheme.typography.displayMedium, color = Green800, modifier = Modifier.fillMaxWidth())
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(data.user?.name!!, style = MaterialTheme.typography.titleMedium, color = Green900)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(data.user?.phoneNumber!!, style = MaterialTheme.typography.displayMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(timeSlot, style = MaterialTheme.typography.displayMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(data.room?.name!!, style = MaterialTheme.typography.displayMedium)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("${data.participant} Orang", style = MaterialTheme.typography.displayMedium)
                                if (!data.description.isNullOrEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(data.description, style = Typography.bodyMedium, maxLines = 2)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Button(
                                onClick = {
                                    scanState.value = null
                                    viewModel.resetValidationState()
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Scan Berikutnya")
                            }
                        }
                    }

                    is ScanState.Error -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Booking Tidak Valid",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                (bookingValidationState.value as ScanState.Error).message
                                    ?: "Terjadi kesalahan",
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    scanState.value = null
                                    viewModel.resetValidationState()
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Coba Lagi")
                            }
                        }
                    }
                    else -> {}
                }
            }

    }
}
