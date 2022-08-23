package com.tutorial.imagefiltersapp.repositiories

import android.graphics.Bitmap
import android.net.Uri
import com.tutorial.imagefiltersapp.models.ImageFilter

interface EditImageRepository {
    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?
    suspend fun getImageFilters(image: Bitmap): List<ImageFilter>
    suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri?
}