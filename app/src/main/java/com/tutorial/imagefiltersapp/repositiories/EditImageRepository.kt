package com.tutorial.imagefiltersapp.repositiories

import android.graphics.Bitmap
import android.net.Uri

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
}