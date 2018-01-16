package com.client.shop.ui.product.di

import com.client.shop.ui.product.ProductDetailsActivity
import dagger.Subcomponent

@Subcomponent(modules = [ProductDetailsModule::class])
interface ProductDetailsComponent {

    fun inject(activity: ProductDetailsActivity)
}