package com.shopapp.ui.product.di

import com.shopapp.ui.product.ProductHorizontalFragment
import dagger.Subcomponent

@Subcomponent(modules = [TestProductHorizontalModule::class])
interface TestProductHorizontalComponent : ProductHorizontalComponent {

    override fun inject(fragment: ProductHorizontalFragment)
}