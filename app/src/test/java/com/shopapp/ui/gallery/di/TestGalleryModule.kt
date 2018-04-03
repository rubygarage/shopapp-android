package com.shopapp.ui.gallery.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.gallery.router.GalleryRouter
import dagger.Module
import dagger.Provides

@Module
class TestGalleryModule {

    @Provides
    fun provideGalleryRouter(): GalleryRouter = mock()

}
