package com.tutorial.imagefiltersapp.viewModels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tutorial.imagefiltersapp.models.SavedImagesDataState
import com.tutorial.imagefiltersapp.repositiories.SavedImagesRepository
import com.tutorial.imagefiltersapp.utilities.Coroutines
import java.io.File

class SavedImagesViewModel(private val savedImagesRepository: SavedImagesRepository): ViewModel() {

    private val savedImageDataState = MutableLiveData<SavedImagesDataState>()

    val savedImageUiState: LiveData<SavedImagesDataState>
        get() {
            return savedImageDataState
        }

    private fun emitSavedImageUiState(
        isLoading: Boolean = false,
        savedImages: List<Pair<File, Bitmap>>? = null,
        error: String? = null
    ) {
        val dataState = SavedImagesDataState(isLoading, savedImages, error)
        savedImageDataState.postValue(dataState)
    }

    fun loadSavedImages() {
        Coroutines.io {
            runCatching {
                emitSavedImageUiState(isLoading = true)
                savedImagesRepository.loadSavedImages()
            }.onSuccess { savedImages ->
                if (savedImages.isNullOrEmpty())
                    emitSavedImageUiState(error = "找不到相片")
                else
                    emitSavedImageUiState(savedImages = savedImages)
            }.onFailure { throwable ->
                emitSavedImageUiState(error = throwable.message.toString())
            }
        }
    }
}