package com.dev.sirasa.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {
    fun saveFileToDownloads(context: Context, inputStream: InputStream, fileName: String): File? {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        return try {
            FileOutputStream(file).use { output ->
                inputStream.copyTo(output)
            }

            // 🔔 Tampilkan Notifikasi dengan Aksi Buka File
            showDownloadNotification(context, fileName, file)

            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun showDownloadNotification(context: Context, fileName: String, file: File) {
        val channelId = "download_channel"
        val notificationId = 1001

        // ✅ **Gunakan context.applicationContext untuk mencegah memory leak**
        val appContext = context.applicationContext

        // 🔍 **Pastikan Izin Notifikasi Sudah Diberikan**
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Notification", "Izin notifikasi belum diberikan!")
            return // 🚫 Jangan lanjutkan jika izin belum diberikan
        }

        // ✅ **Gunakan FileProvider agar tidak terkena FileUriExposedException**
        val fileUri: Uri = FileProvider.getUriForFile(appContext, "${appContext.packageName}.provider", file)

        val openFileIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(fileUri, getMimeType(fileName))
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            appContext,
            0,
            openFileIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 🔔 **Buat Notification Channel untuk Android 8+**
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Download Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = appContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // 🚀 **Buat Notifikasi**
        val notification = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle("Download Selesai")
            .setContentText("File $fileName telah disimpan di Download")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent) // 🔥 **Klik Notifikasi akan Membuka File**
            .build()

        NotificationManagerCompat.from(appContext).notify(notificationId, notification)
    }

    // Fungsi untuk mendapatkan MIME Type berdasarkan ekstensi file
    fun getMimeType(fileName: String): String {
        return when {
            fileName.endsWith(".pdf") -> "application/pdf"
            fileName.endsWith(".xlsx") -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            fileName.endsWith(".csv") -> "text/csv"
            else -> "*/*"
        }
    }
}
