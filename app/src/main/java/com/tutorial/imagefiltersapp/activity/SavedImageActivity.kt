package com.tutorial.imagefiltersapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import com.tutorial.imagefiltersapp.KEY_FILTERED_IMAGE_URI
import com.tutorial.imagefiltersapp.adapter.SavedImagesAdapter
import com.tutorial.imagefiltersapp.databinding.ActivitySavedImageBinding
import com.tutorial.imagefiltersapp.helper.displayToast
import com.tutorial.imagefiltersapp.viewModels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SavedImageActivity : AppCompatActivity(), SavedImagesAdapter.SavedImageListener {
    private lateinit var binding: ActivitySavedImageBinding
    private val viewModel: SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
        setupObserver()
        viewModel.loadSavedImages()
    }

    override fun onImageClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.provider",
            file
        )
        Intent(
            applicationContext,
            FilteredImageActivity::class.java
        ).also { filteredImageIntent ->
            filteredImageIntent.putExtra(KEY_FILTERED_IMAGE_URI, fileUri)
            startActivity(filteredImageIntent)
        }
    }

    private fun setupObserver() {
        viewModel.savedImageUiState.observe(this) {
            val savedImageDataState = it ?: return@observe
            binding.pbSavedImages.visibility =
                if (savedImageDataState.isLoading == true) View.VISIBLE else View.GONE
            savedImageDataState.savedImages?.let { savedImages ->
                displayToast("載入 ${savedImages.size} 張")
                SavedImagesAdapter(savedImages, this).also { adapter ->
                    with(binding.rvSavedImages) {
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }
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