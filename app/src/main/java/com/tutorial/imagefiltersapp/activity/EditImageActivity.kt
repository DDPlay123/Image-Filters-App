package com.tutorial.imagefiltersapp.activity

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tutorial.imagefiltersapp.KEY_IMAGE_URI
import com.tutorial.imagefiltersapp.databinding.ActivityEditImageBinding
import com.tutorial.imagefiltersapp.helper.displayToast
import com.tutorial.imagefiltersapp.helper.show
import com.tutorial.imagefiltersapp.viewModels.EditImageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditImageBinding
    private val viewModel: EditImageViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
        // 先將URI放到ViewModel設定狀態，再觀察LiveData的變化
        setupObservers()
        prepareImagePreview()
    }

    private fun setListener() {
        binding.run {
            layoutBack.setOnClickListener { onBackPressed() }
        }
    }

    // 設定觀察者，當狀態改變，產生對應動作
    private fun setupObservers() {
        viewModel.imagePreviewUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.pbPreview.visibility =
                if (dataState.isLoading == true) View.VISIBLE else View.GONE

            dataState.bitmap?.let { bitmap ->
                binding.imagePreview.setImageBitmap(bitmap)
                binding.imagePreview.show()
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    // 導入照片 URI 至 viewModel 設定狀態
    private fun prepareImagePreview() {
        intent.getParcelableExtra<Uri>(KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }
}