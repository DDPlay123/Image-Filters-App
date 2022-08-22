package com.tutorial.imagefiltersapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.imagefiltersapp.databinding.ItemContainerFilterBinding
import com.tutorial.imagefiltersapp.models.ImageFilter

class ImageFiltersAdapter(private val imageFilters: List<ImageFilter>):
    RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        return ImageFilterViewHolder(ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
        with(holder) {
            with(imageFilters[position]) {
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.tvFilterName.text = name
            }
        }
    }

    override fun getItemCount(): Int = imageFilters.size

    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding)
        : RecyclerView.ViewHolder(binding.root)
}