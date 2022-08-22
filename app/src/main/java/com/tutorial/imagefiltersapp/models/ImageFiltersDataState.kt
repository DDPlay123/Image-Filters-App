package com.tutorial.imagefiltersapp.models

data class ImageFiltersDataState (
        var isLoading: Boolean? = false,
        var imageFilters: List<ImageFilter>? = null,
        var error: String? = null
)
