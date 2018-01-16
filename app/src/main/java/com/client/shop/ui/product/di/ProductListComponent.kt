package com.client.shop.ui.product.di

import com.client.shop.ui.product.ProductListActivity
import dagger.Subcomponent

@Subcomponent(modules = [ProductListModule::class])
interface ProductListComponent {

    fun inject(activity: ProductListActivity)
}