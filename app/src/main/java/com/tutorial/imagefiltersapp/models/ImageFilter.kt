package com.tutorial.imagefiltersapp.models

import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

data class ImageFilter (
    var name: String? = null,
    var filter: GPUImageFilter? = null,
    var filterPreview: Bitmap? = null
)