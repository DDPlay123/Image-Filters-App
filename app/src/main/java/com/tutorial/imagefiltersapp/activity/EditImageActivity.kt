package com.tutorial.imagefiltersapp.activity

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tutorial.imagefiltersapp.KEY_IMAGE_URI
import com.tutorial.imagefiltersapp.R
import com.tutorial.imagefiltersapp.databinding.ActivityEditImageBinding
import java.io.InputStream

class EditImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
        displayImagePreview()
    }

    private fun setListener() {
        binding.run {
            layoutBack.setOnClickListener { onBackPressed() }
        }
    }

    private fun displayImagePreview() {
        intent.getParcelableExtra<Uri>(KEY_IMAGE_URI)?.let { imageUri ->
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.imagePreview.setImageBitmap(bitmap)
            binding.imagePreview.visibility = View.VISIBLE
        }
    }
}