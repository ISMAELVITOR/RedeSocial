package com.example.redesocial.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageUtils {

    fun bitmapToBase64(bitmap: Bitmap, quality: Int = 60): String {
        val outputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        val bytes = outputStream.toByteArray()

        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun resizeBitmap(bitmap: Bitmap, maxSize: Int = 1024): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxSize && height <= maxSize) {
            return bitmap
        }

        val scale = minOf(maxSize.toFloat() / width, maxSize.toFloat() / height)
        val newWidth = (width * scale).toInt().coerceAtLeast(1)
        val newHeight = (height * scale).toInt().coerceAtLeast(1)

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    fun base64ToBitmap(base64: String): Bitmap {
        val decodedBytes = Base64.decode(base64, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(
            decodedBytes,
            0,
            decodedBytes.size
        )
    }
}
