package com.shopapp.ui.product.di

import com.shopapp.ui.product.ProductDetailsActivity
import com.shopapp.ui.product.ProductHorizontalFragment
import com.shopapp.ui.product.ProductListActivity
import com.shopapp.ui.product.ProductPopularFragment
import dagger.Subcomponent

@Subcomponent(modules = [TestProductModule::class])
interface TestProductComponent : ProductComponent {

    override fun inject(activity: ProductDetailsActivity)

    override fun inject(activity: ProductListActivity)

    override fun inject(fragment: ProductHorizontalFragment)

    override fun inject(fragment: ProductPopularFragment)
}