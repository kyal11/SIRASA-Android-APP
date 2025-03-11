package com.dev.sirasa.utils

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeReader

class QRCodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val qrCodeReader = QRCodeReader()

    override fun analyze(image: ImageProxy) {
        val buffer = image.planes[0].buffer
        val data = ByteArray(buffer.remaining())
        buffer.get(data)

        val source = PlanarYUVLuminanceSource(
            data,
            image.width,
            image.height,
            0,
            0,
            image.width,
            image.height,
            false
        )

        val bitmap = BinaryBitmap(HybridBinarizer(source))

        try {
            val result = qrCodeReader.decode(bitmap)
            onQrCodeScanned(result.text)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            image.close()
        }
    }
}