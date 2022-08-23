package com.tutorial.imagefiltersapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tutorial.imagefiltersapp.databinding.ActivitySavedImageBinding
import com.tutorial.imagefiltersapp.helper.displayToast
import com.tutorial.imagefiltersapp.viewModels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedImageBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserver()
        viewModel.loadSavedImages()
    }

    private fun setupObserver() {
        viewModel.savedImageUiState.observe(this) {
            val savedImageDataState = it ?: return@observe
            binding.pbSavedImages.visibility =
                if (savedImageDataState.isLoading == true) View.VISIBLE else View.GONE
            savedImageDataState.savedImages?.let { savedImages ->
                displayToast("載入 ${savedImages.size} 張")
            } ?: run {
                savedImageDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun setListener() {
        binding.run {
            btnBack.setOnClickListener { onBackPressed() }
        }
    }
}