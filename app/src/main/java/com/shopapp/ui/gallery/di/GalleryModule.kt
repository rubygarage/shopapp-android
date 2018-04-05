package com.shopapp.ui.gallery.di

import com.shopapp.ui.gallery.router.GalleryRouter
import dagger.Module
import dagger.Provides

@Module
class GalleryModule {

    @Provides
    fun provideGalleryRouter(): GalleryRouter = GalleryRouter()

}
