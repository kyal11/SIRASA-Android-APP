package com.dev.sirasa.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageCompressor {
    // Maximum file size (500 KB)
    private const val MAX_FILE_SIZE = 500 * 1024

    // Maximum image dimensions
    private const val MAX_WIDTH = 1024
    private const val MAX_HEIGHT = 1024

    /**
     * Compress and resize image with size and quality constraints
     * @param context Application context
     * @param imageUri Uri of the original image
     * @return Compressed image file
     */
    suspend fun compressImage(context: Context, imageUri: Uri): File = withContext(Dispatchers.IO) {
        try {
            // Read bitmap with optimal sampling
            val originalBitmap = decodeSampledBitmapFromUri(context, imageUri)

            // Rotate bitmap if needed (handle image orientation)
            val rotatedBitmap = rotateBitmapIfRequired(context, imageUri, originalBitmap)

            // Resize bitmap
            val resizedBitmap = resizeBitmap(rotatedBitmap)

            // Compress bitmap
            return@withContext compressBitmapToFile(context, resizedBitmap)
        } catch (e: Exception) {
            Log.e("ImageCompressor", "Error compressing image", e)
            throw e
        }
    }

    /**
     * Decode bitmap with optimal sampling to reduce memory usage
     */
    private fun decodeSampledBitmapFromUri(context: Context, uri: Uri): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT)

        // Decode bitmap with calculated sample size
        options.inJustDecodeBounds = false
        return context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        } ?: throw IOException("Cannot open input stream")
    }

    /**
     * Calculate sample size for bitmap decoding
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2
            // and keeps both height and width larger than the requested height and width
            while ((halfHeight / inSampleSize) >= reqHeight &&
                (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * Rotate bitmap based on EXIF orientation
     */
    private fun rotateBitmapIfRequired(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.use {
            val exif = ExifInterface(it)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                else -> return bitmap
            }

            return Bitmap.createBitmap(
                bitmap, 0, 0,
                bitmap.width, bitmap.height,
                matrix, true
            )
        }
        return bitmap
    }

    /**
     * Resize bitmap maintaining aspect ratio
     */
    private fun resizeBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        // Calculate scale
        val scaleWidth = MAX_WIDTH.toFloat() / width
        val scaleHeight = MAX_HEIGHT.toFloat() / height
        val scale = minOf(scaleWidth, scaleHeight)

        // Prevent upscaling
        if (scale >= 1f) return bitmap

        // Create scaled bitmap
        val matrix = Matrix()
        matrix.postScale(scale, scale)

        return Bitmap.createBitmap(
            bitmap, 0, 0,
            width, height,
            matrix, true
        )
    }

    /**
     * Compress bitmap to file with quality adjustment
     */
    private fun compressBitmapToFile(context: Context, bitmap: Bitmap): File {
        // Create temporary file
        val outputFile = File(context.cacheDir, "compressed_image_${System.currentTimeMillis()}.jpg")

        var quality = 100
        val outputStream = ByteArrayOutputStream()

        // Compress with quality adjustment
        do {
            // Reset output stream
            outputStream.reset()

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

            // Reduce quality for next iteration
            quality -= 5
        } while (outputStream.size() > MAX_FILE_SIZE && quality > 0)

        // Write to file
        FileOutputStream(outputFile).use { it.write(outputStream.toByteArray()) }

        return outputFile
    }

    /**
     * Additional method to get image dimensions without loading full bitmap
     */
    fun getImageDimensions(context: Context, uri: Uri): Pair<Int, Int> {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }
        return Pair(options.outWidth, options.outHeight)
    }
}