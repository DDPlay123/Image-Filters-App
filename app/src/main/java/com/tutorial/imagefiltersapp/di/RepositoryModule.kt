package com.tutorial.imagefiltersapp.di

import com.tutorial.imagefiltersapp.repositiories.EditImageRepository
import com.tutorial.imagefiltersapp.repositiories.EditImageRepositoryImpl
import com.tutorial.imagefiltersapp.repositiories.SavedImagesRepository
import com.tutorial.imagefiltersapp.repositiories.SavedImagesRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

// Koin Dependency injection
val repositoryModule = module {
    factory<EditImageRepository> { EditImageRepositoryImpl(androidContext()) }
    factory<SavedImagesRepository> { SavedImagesRepositoryImpl(androidContext()) }
}