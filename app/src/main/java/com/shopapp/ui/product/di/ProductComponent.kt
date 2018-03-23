package com.shopapp.ui.product.di

import com.shopapp.ui.product.ProductDetailsActivity
import com.shopapp.ui.product.ProductHorizontalFragment
import com.shopapp.ui.product.ProductListActivity
import com.shopapp.ui.product.ProductPopularFragment
import dagger.Subcomponent

@Subcomponent(modules = [ProductModule::class])
interface ProductComponent {

    fun inject(activity: ProductDetailsActivity)

    fun inject(activity: ProductListActivity)

    fun inject(fragment: ProductHorizontalFragment)

    fun inject(fragment: ProductPopularFragment)
}