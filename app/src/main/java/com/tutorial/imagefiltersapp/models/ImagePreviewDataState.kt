package com.tutorial.imagefiltersapp.models

import android.graphics.Bitmap

data class ImagePreviewDataState(
    var isLoading: Boolean? = false,
    var bitmap: Bitmap? = null,
    var error: String? = null
)
