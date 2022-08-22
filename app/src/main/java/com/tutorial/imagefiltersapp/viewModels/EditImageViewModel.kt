package com.tutorial.imagefiltersapp.viewModels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tutorial.imagefiltersapp.models.ImagePreviewDataState
import com.tutorial.imagefiltersapp.repositiories.EditImageRepository
import com.tutorial.imagefiltersapp.utilities.Coroutines

class EditImageViewModel(private val editImageRepository: EditImageRepository): ViewModel() {

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
                emitPreviewUiState(error = throwable.message)
            }
        }
    }
}