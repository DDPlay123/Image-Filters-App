package com.tutorial.imagefiltersapp.viewModels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tutorial.imagefiltersapp.models.ImageFilter
import com.tutorial.imagefiltersapp.models.ImageFiltersDataState
import com.tutorial.imagefiltersapp.models.ImagePreviewDataState
import com.tutorial.imagefiltersapp.models.SaveFilteredImageDataState
import com.tutorial.imagefiltersapp.repositiories.EditImageRepository
import com.tutorial.imagefiltersapp.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository): ViewModel() {

    // region::  圖片預覽

    private val imagePreviewState = MutableLiveData<ImagePreviewDataState>()

    // 取得 imagePreviewState
    val imagePreviewUiState: LiveData<ImagePreviewDataState>
        get() {
            return imagePreviewState
        }

    // 設定狀態至 imagePreviewState
    private fun emitPreviewUiState(isLoading: Boolean = false,
                                   bitmap: Bitmap? = null,
                                   error: String? = null) {
        val dataState = ImagePreviewDataState(isLoading, bitmap, error)
        imagePreviewState.postValue(dataState)
    }

    fun prepareImagePreview(imageUri: Uri) {
        Coroutines.io {
            runCatching {
                emitPreviewUiState(isLoading = true)
                editImageRepository.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null)
                    emitPreviewUiState(bitmap = bitmap)
                else
                    emitPreviewUiState(error = "無法獲得相片預覽")
            }.onFailure { throwable ->
                emitPreviewUiState(error = throwable.message.toString())
            }
        }
    }
    // endregion

    // region::  載入濾鏡
    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataState>()

    // 取得 imageFiltersDataState
    val imageFiltersUiState: LiveData<ImageFiltersDataState>
        get() {
            return imageFiltersDataState
        }

    // 設定狀態至 imageFiltersDataState
    private fun emitImageFiltersUiState(isLoading: Boolean = false,
                                        imageFilters: List<ImageFilter>? = null,
                                        error: String? = null) {
        val dataState = ImageFiltersDataState(isLoading, imageFilters, error)
        imageFiltersDataState.postValue(dataState)
    }

    // 取得預覽圖
    private fun getPreviewImage(originalImage: Bitmap): Bitmap {
        return runCatching {
            val previewWidth = 150
            val previewHeight = originalImage.height * previewWidth / originalImage.width

            Bitmap.createScaledBitmap(originalImage, previewWidth, previewHeight, false)
        }.getOrDefault(originalImage)
    }

    fun loadImageFilters(originalImage: Bitmap) {
        Coroutines.io {
            runCatching {
                emitImageFiltersUiState(isLoading = true)
                editImageRepository.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess { imageFilters ->
                emitImageFiltersUiState(imageFilters = imageFilters)
            }.onFailure { throwable ->
                emitImageFiltersUiState(error = throwable.message.toString())
            }
        }
    }
    // endregion

    // region::  儲存圖片
    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDataState>()

    // 取得 saveFilteredImageDataState
    val saveFilteredImageUiState: LiveData<SaveFilteredImageDataState>
        get() {
            return saveFilteredImageDataState
        }

    // 設定狀態至 saveFilteredImageDataState
    private fun emitSaveFilteredImageUiState(isLoading: Boolean = false,
                                             uri: Uri? = null,
                                             error: String? = null) {
        val dataState = SaveFilteredImageDataState(isLoading, uri, error)
        saveFilteredImageDataState.postValue(dataState)
    }

    fun saveFilteredImage(filterBitmap: Bitmap) {
        Coroutines.io {
            runCatching {
                emitSaveFilteredImageUiState(isLoading = true)
                editImageRepository.saveFilteredImage(filterBitmap)
            }.onSuccess { savedImageUri ->
                if (savedImageUri != null)
                    emitSaveFilteredImageUiState(uri = savedImageUri)
            }.onFailure { throwable ->
                emitPreviewUiState(error = throwable.message.toString())
            }
        }
    }
    // endregion
}