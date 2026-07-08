package com.example.redesocial.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64

object ImageUtils {
    fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return runCatching {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }.getOrNull()
    }

    fun base64ToBitmapOrNull(base64: String): Bitmap? {
        return try {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            null
        }
    }

    fun resizeKeepingAspectRatio(bitmap: Bitmap, maxSize: Int): Bitmap {
        val currentWidth = bitmap.width
        val currentHeight = bitmap.height

        if (currentWidth <= maxSize && currentHeight <= maxSize) {
            return bitmap
        }

        val scale = minOf(maxSize.toFloat() / currentWidth, maxSize.toFloat() / currentHeight)
        val targetWidth = (currentWidth * scale).toInt().coerceAtLeast(1)
        val targetHeight = (currentHeight * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }

    fun bitmapToBase64Jpeg(bitmap: Bitmap, quality: Int): String {
        val outputStream = java.io.ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }
}
