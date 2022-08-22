package com.tutorial.imagefiltersapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.tutorial.imagefiltersapp.KEY_IMAGE_URI
import com.tutorial.imagefiltersapp.Request_PERMISSION
import com.tutorial.imagefiltersapp.databinding.ActivityMainBinding
import com.tutorial.imagefiltersapp.manager.ToastManager
import com.tutorial.imagefiltersapp.utilities.Method

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListener()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Request_PERMISSION && grantResults.isNotEmpty())
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                ToastManager.instance.showToast(this, "沒有權限", true)
    }

    private fun setListener() {
        binding.run {
            btnEditNewImage.setOnClickListener {
                if (!Method.hasPermissions(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE))
                    Method.requestPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                else
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    ).also { pickIntent ->
                        pickIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        mPickImage.launch(pickIntent)
                    }
            }
        }
    }

    private val mPickImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
        if (result != null) {
            try {
                val imageUri: Uri? = result.data!!.data

                Intent(applicationContext, EditImageActivity::class.java).also { editImageIntent ->
                    editImageIntent.putExtra(KEY_IMAGE_URI, imageUri)
                    startActivity(editImageIntent)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}