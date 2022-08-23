package com.tutorial.imagefiltersapp.activity

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.tutorial.imagefiltersapp.KEY_IMAGE_URI
import com.tutorial.imagefiltersapp.adapter.ImageFiltersAdapter
import com.tutorial.imagefiltersapp.databinding.ActivityEditImageBinding
import com.tutorial.imagefiltersapp.helper.displayToast
import com.tutorial.imagefiltersapp.helper.show
import com.tutorial.imagefiltersapp.models.ImageFilter
import com.tutorial.imagefiltersapp.models.ImageFiltersDataState
import com.tutorial.imagefiltersapp.viewModels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity(), ImageFiltersAdapter.ImageFilterListener {
    private lateinit var binding: ActivityEditImageBinding
    private val viewModel: EditImageViewModel by viewModel()

    private lateinit var gpuImage: GPUImage
    private lateinit var originalBitmap: Bitmap
    private val filteredBitmap = MutableLiveData<Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
        // 先將URI放到ViewModel設定狀態，再觀察LiveData的變化
        setupObservers()
        prepareImagePreview()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level <= TRIM_MEMORY_BACKGROUND)
            System.gc()
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter) {
            with(gpuImage) {
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }

    private fun setListener() {
        binding.run {
            btnBack.setOnClickListener { onBackPressed() }

            imagePreview.setOnLongClickListener {
                imagePreview.setImageBitmap(originalBitmap)
                return@setOnLongClickListener false
            }

            imagePreview.setOnClickListener {
                imagePreview.setImageBitmap(filteredBitmap.value)
            }
        }
    }

    // 設定觀察者，當狀態改變，產生對應動作
    private fun setupObservers() {
        viewModel.imagePreviewUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.pbPreview.visibility =
                if (dataState.isLoading == true) View.VISIBLE else View.GONE

            dataState.bitmap?.let { bitmap ->
                // 第一張濾鏡為原始圖
                originalBitmap = bitmap
                filteredBitmap.value = bitmap

                with(originalBitmap) {
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(this)
                }
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }

        viewModel.imageFiltersUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.pbFilters.visibility =
                if (dataState.isLoading == true) View.VISIBLE else View.GONE

            dataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters, this).also { adapter ->
                    binding.rvFilters.adapter = adapter
                }
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }

        filteredBitmap.observe(this) { bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)
        }
    }

    // 導入照片 URI 至 viewModel 設定狀態
    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext) // 初始化
        intent.getParcelableExtra<Uri>(KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }
}