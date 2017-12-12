package com.client.shop.ui.product.di

import com.client.shop.ui.product.ProductListActivity
import dagger.Subcomponent

@Subcomponent(modules = [ProductModule::class])
interface ProductComponent {

    fun inject(activity: ProductListActivity)
}