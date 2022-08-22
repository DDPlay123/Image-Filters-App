package com.tutorial.imagefiltersapp.repositiories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.InputStream

// 搭被 Koin DI
class EditImageRepositoryImpl(private val context: Context): EditImageRepository {

    override suspend fun prepareImagePreview(imageUri: Uri): Bitmap? {
        getInputStreamFromUri(imageUri)?.let { inputStream ->
            val originalBitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
            val width: Int = context.resources.displayMetrics.widthPixels
            val height: Int = ((originalBitmap.height * width) / originalBitmap.width)
            return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
        } ?: return null
    }

    private fun getInputStreamFromUri(uri: Uri): InputStream? =
        context.contentResolver.openInputStream(uri)
}