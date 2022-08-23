package com.tutorial.imagefiltersapp.models

import android.graphics.Bitmap
import java.io.File

data class SavedImagesDataState (
    var isLoading: Boolean? = false,
    var savedImages: List<Pair<File, Bitmap>>? = null,
    var error: String? = null
)