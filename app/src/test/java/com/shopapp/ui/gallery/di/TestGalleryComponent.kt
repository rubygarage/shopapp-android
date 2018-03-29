package com.shopapp.ui.gallery.di

import com.shopapp.ui.gallery.GalleryFragment
import dagger.Subcomponent

@Subcomponent(modules = [TestGalleryModule::class])
interface TestGalleryComponent : GalleryComponent {

    override fun inject(fragment: GalleryFragment)
}