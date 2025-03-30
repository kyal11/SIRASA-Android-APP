package com.dev.sirasa.screens.common.contact

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.dev.sirasa.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.dev.sirasa.ui.theme.Green700
import com.dev.sirasa.ui.theme.SirasaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactUsScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Hubungi Kami") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Banner Image
            Image(
                painter = painterResource(id = R.drawable.banner_perpus),
                contentDescription = "Library Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

//            // Logo and Title
//            Image(
//                painter = painterResource(id = R.drawable.logo_perpus),
//                contentDescription = "Library Logo",
//                modifier = Modifier
//                    .size(120.dp)
//            )

            Text(
                text = "UPT Perpustakaan UPN Veteran Jakarta",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Text(
                text = "Unit pelaksana teknis di bidang perpustakaan yang mempunyai tugas melaksanakan pemberian layanan kepustakaan.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Contact Information
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp,
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(2.dp, Green700)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Contact Information",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Address Section
                    ContactItem(
                        icon = Icons.Default.LocationOn,
                        title = "Address",
                        content = "Kampus UPN \"Veteran\" Jakarta Gedung DR. Soetomo Lt. 3 dan Lt. 4 Jl. R.S. Fatmawati Pondok Labu - Jakarta Selatan 12450"
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Phone Section
                    ContactItem(
                        icon = Icons.Default.Phone,
                        title = "Phone",
                        content = "021-75902835",
                        onClick = {
                            uriHandler.openUri("tel:02175902835")
                        }
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // WhatsApp Section
                    ContactItemWithDrawable(
                        drawableResId = R.drawable.wa_icon,
                        title = "WhatsApp",
                        content = "085811591970\n085811591971"
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Email Section
                    ContactItem(
                        icon = Icons.Default.Email,
                        title = "Email",
                        content = "library@upnvj.ac.id",
                        isClickable = true,
                        onClick = {
                            uriHandler.openUri("mailto:library@upnvj.ac.id")
                        }
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Location Section
                    ContactItem(
                        icon = Icons.Default.LocationOn,
                        title = "Location",
                        content = "MQMV+GQ Pd. Labu, Kota Jakarta Selatan, Daerah Khusus Ibukota Jakarta",
                        isClickable = true,
                        onClick = {
                            uriHandler.openUri("https://maps.app.goo.gl/tEDYJFevQTK6GfzA9")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Social Media Links
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp,
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = BorderStroke(2.dp, Green700)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Social Media",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Instagram
                    SocialMediaItemWithDrawable(
                        drawableResId = R.drawable.ig_icon,
                        platform = "Instagram",
                        handle = "perpustakaanupnvj",
                        onClick = {
                            uriHandler.openUri("https://www.instagram.com/perpustakaanupnvj/")
                        }
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // YouTube
                    SocialMediaItemWithDrawable(
                        drawableResId = R.drawable.yt_icon,
                        platform = "YouTube",
                        handle = "perpustakaanupnvj",
                        onClick = {
                            uriHandler.openUri("https://www.youtube.com/@perpustakaanupnvj")
                        }
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Facebook
                    SocialMediaItemWithDrawable(
                        drawableResId = R.drawable.fb_icon,
                        platform = "Facebook",
                        handle = "perpustakaanupnvj",
                        onClick = {
                            uriHandler.openUri("https://www.facebook.com/perpustakaanupnvj/")
                        }
                    )

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Website
                    SocialMediaItemWithDrawable(
                        drawableResId = R.drawable.web_icon,
                        platform = "Website",
                        handle = "perpustakaan.upnvj.ac.id",
                        onClick = {
                            uriHandler.openUri("https://perpustakaan.upnvj.ac.id")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactPreview(){
    SirasaTheme {
        ContactUsScreen {

        }
    }
}
@Composable
fun ContactItem(
    icon: ImageVector,
    title: String,
    content: String,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (isClickable) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(start = 32.dp)
                    .clickable { onClick() },
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        } else {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 32.dp)
            )
        }
    }
}

@Composable
fun ContactItemWithDrawable(
    drawableResId: Int,
    title: String,
    content: String
) {
    val context = LocalContext.current
    val numbers = content.split("\n") // Pisahkan nomor berdasarkan enter

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = drawableResId),
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        numbers.forEach { number ->
            Text(
                text = number,
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 32.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:$number") // Membuka aplikasi kontak dengan nomor ini
                        }
                        context.startActivity(intent)
                    }
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


@Composable
fun SocialMediaItemWithDrawable(
    drawableResId: Int,
    platform: String,
    handle: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = drawableResId),
            contentDescription = platform,
            modifier = Modifier.size(24.dp)
        )

        Column(
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = platform,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = handle,
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}