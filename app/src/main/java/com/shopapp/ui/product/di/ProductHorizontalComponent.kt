package com.shopapp.ui.product.di

import com.shopapp.ui.product.ProductHorizontalFragment
import dagger.Subcomponent

@Subcomponent(modules = [ProductHorizontalModule::class])
interface ProductHorizontalComponent {

    fun inject(fragment: ProductHorizontalFragment)
}