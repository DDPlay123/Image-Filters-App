package com.tutorial.imagefiltersapp.models

import android.net.Uri

data class SaveFilteredImageDataState(
    var isLoading: Boolean? = false,
    var uri: Uri? = null,
    var error: String? = null
)

