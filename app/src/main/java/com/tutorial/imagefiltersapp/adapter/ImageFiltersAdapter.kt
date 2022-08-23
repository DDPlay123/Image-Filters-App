package com.tutorial.imagefiltersapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.imagefiltersapp.R
import com.tutorial.imagefiltersapp.databinding.ItemContainerFilterBinding
import com.tutorial.imagefiltersapp.models.ImageFilter

class ImageFiltersAdapter(
    private val imageFilters: List<ImageFilter>,
    private val imageFilterListener: ImageFilterListener):
    RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>() {

    private var selectedFilterPosition = 0
    private var previouslySelectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        return ImageFilterViewHolder(ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            with(imageFilters[position]) {
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.tvFilterName.text = name
                binding.root.setOnClickListener {
                    if (position != selectedFilterPosition) {
                        imageFilterListener.onFilterSelected(this)
                        previouslySelectedPosition = selectedFilterPosition
                        selectedFilterPosition = position
                        with(this@ImageFiltersAdapter) {
                            notifyItemChanged(previouslySelectedPosition, Unit)
                            notifyItemChanged(selectedFilterPosition, Unit)
                        }
                    }
                }
            }
            binding.tvFilterName.setTextColor(
                ContextCompat.getColor(
                    binding.tvFilterName.context,
                    if (selectedFilterPosition == position)
                        R.color.primaryDark
                    else
                        R.color.primaryText
                )
            )
        }
    }

    override fun getItemCount(): Int = imageFilters.size

    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding)
        : RecyclerView.ViewHolder(binding.root)

    interface ImageFilterListener {
        fun onFilterSelected(imageFilter: ImageFilter)
    }
}