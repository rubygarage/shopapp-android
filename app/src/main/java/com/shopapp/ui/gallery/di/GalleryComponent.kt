package com.shopapp.ui.gallery.di

import com.shopapp.ui.gallery.GalleryFragment
import dagger.Subcomponent

@Subcomponent(modules = [GalleryModule::class])
interface GalleryComponent {

    fun inject(fragment: GalleryFragment)
}