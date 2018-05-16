package com.shopapp.ui.product.di

import com.shopapp.ui.product.ProductDetailsActivity
import com.shopapp.ui.product.ProductListActivity
import com.shopapp.ui.product.ProductPopularFragment
import com.shopapp.ui.product.ProductShortcutFragment
import dagger.Subcomponent

@Subcomponent(modules = [ProductModule::class])
interface ProductComponent {

    fun inject(activity: ProductDetailsActivity)

    fun inject(activity: ProductListActivity)

    fun inject(fragment: ProductShortcutFragment)

    fun inject(fragment: ProductPopularFragment)
}