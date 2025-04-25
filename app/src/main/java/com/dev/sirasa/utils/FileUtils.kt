package com.dev.sirasa.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtils {
    /**
     * Menyimpan file ke direktori Download menggunakan MediaStore API
     * untuk Android 10+ dan metode tradisional untuk versi sebelumnya
     */
    fun saveFileToDownloads(context: Context, inputStream: InputStream, fileName: String): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Untuk Android 10+, gunakan MediaStore API
            saveWithMediaStore(context, inputStream, fileName)
        } else {
            // Untuk Android 9 dan sebelumnya, gunakan pendekatan tradisional
            saveWithLegacyMethod(context, inputStream, fileName)
        }
    }

    /**
     * Menyimpan file menggunakan MediaStore API (Android 10+)
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveWithMediaStore(context: Context, inputStream: InputStream, fileName: String): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, getMimeType(fileName))
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        return uri?.let {
            try {
                resolver.openOutputStream(it)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                contentValues.clear()
                contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
                resolver.update(it, contentValues, null, null)

                // Tampilkan notifikasi download selesai
                showDownloadNotification(context, fileName, it)

                it // Return URI dari file yang berhasil disimpan
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    /**
     * Menyimpan file menggunakan metode tradisional (Android 9 dan sebelumnya)
     */
    private fun saveWithLegacyMethod(context: Context, inputStream: InputStream, fileName: String): Uri? {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)

        return try {
            FileOutputStream(file).use { output ->
                inputStream.copyTo(output)
            }

            // Dapatkan URI melalui FileProvider untuk menghindari FileUriExposedException
            val fileUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            // Tampilkan notifikasi download selesai
            showDownloadNotification(context, fileName, fileUri)

            fileUri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Menampilkan notifikasi bahwa download telah selesai dengan opsi untuk membuka file
     */
    private fun showDownloadNotification(context: Context, fileName: String, fileUri: Uri) {
        val channelId = "download_channel"
        val notificationId = 1001

        // Gunakan context.applicationContext untuk mencegah memory leak
        val appContext = context.applicationContext

        // Periksa izin notifikasi untuk Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Notification", "Izin notifikasi belum diberikan!")
            return
        }

        // Siapkan intent untuk membuka file
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

        // Buat notification channel untuk Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Download Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi untuk unduhan file yang selesai"
            }
            val manager = appContext.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // Buat dan tampilkan notifikasi
        val notification = NotificationCompat.Builder(appContext, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle("Download Selesai")
            .setContentText("File $fileName telah disimpan di Download")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(appContext).notify(notificationId, notification)
    }

    /**
     * Mendapatkan MIME Type berdasarkan ekstensi file
     */
    fun getMimeType(fileName: String): String {
        return when {
            fileName.endsWith(".pdf", ignoreCase = true) -> "application/pdf"
            fileName.endsWith(".xlsx", ignoreCase = true) -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            fileName.endsWith(".xls", ignoreCase = true) -> "application/vnd.ms-excel"
            fileName.endsWith(".csv", ignoreCase = true) -> "text/csv"
            fileName.endsWith(".doc", ignoreCase = true) -> "application/msword"
            fileName.endsWith(".docx", ignoreCase = true) -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            fileName.endsWith(".txt", ignoreCase = true) -> "text/plain"
            fileName.endsWith(".jpg", ignoreCase = true) -> "image/jpeg"
            fileName.endsWith(".jpeg", ignoreCase = true) -> "image/jpeg"
            fileName.endsWith(".png", ignoreCase = true) -> "image/png"
            fileName.endsWith(".mp4", ignoreCase = true) -> "video/mp4"
            fileName.endsWith(".mp3", ignoreCase = true) -> "audio/mpeg"
            else -> "*/*"
        }
    }

    /**
     * Memastikan bahwa izin penyimpanan telah diberikan
     * Gunakan fungsi ini sebelum operasi penyimpanan
     */
    fun hasStoragePermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Untuk Android 10+, kita tidak memerlukan izin WRITE_EXTERNAL_STORAGE untuk
            // MediaStore.Downloads menggunakan Scoped Storage
            true
        } else {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
}