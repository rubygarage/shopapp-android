package com.client.shop.ui.product.di

import com.client.shop.ui.product.ProductHorizontalFragment
import dagger.Subcomponent

@Subcomponent(modules = [ProductHorizontalModule::class])
interface ProductHorizontalComponent {

    fun inject(fragment: ProductHorizontalFragment)
}