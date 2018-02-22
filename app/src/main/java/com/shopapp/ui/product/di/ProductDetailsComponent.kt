package com.shopapp.ui.product.di

import com.shopapp.ui.product.ProductDetailsActivity
import dagger.Subcomponent

@Subcomponent(modules = [ProductDetailsModule::class])
interface ProductDetailsComponent {

    fun inject(activity: ProductDetailsActivity)
}