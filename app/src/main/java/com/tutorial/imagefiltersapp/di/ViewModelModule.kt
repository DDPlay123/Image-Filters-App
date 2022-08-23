package com.tutorial.imagefiltersapp.di

import com.tutorial.imagefiltersapp.viewModels.EditImageViewModel
import com.tutorial.imagefiltersapp.viewModels.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EditImageViewModel(editImageRepository = get()) }
    viewModel { SavedImagesViewModel(savedImagesRepository = get()) }
}