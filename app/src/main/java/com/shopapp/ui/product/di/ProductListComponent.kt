package com.shopapp.ui.product.di

import com.shopapp.ui.product.ProductListActivity
import dagger.Subcomponent

@Subcomponent(modules = [ProductListModule::class])
interface ProductListComponent {

    fun inject(activity: ProductListActivity)
}