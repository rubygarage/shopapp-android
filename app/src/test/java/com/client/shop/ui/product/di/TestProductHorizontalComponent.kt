package com.client.shop.ui.product.di

import com.client.shop.ui.product.ProductHorizontalFragment
import dagger.Subcomponent

@Subcomponent(modules = [TestProductHorizontalModule::class])
interface TestProductHorizontalComponent : ProductHorizontalComponent {

    override fun inject(fragment: ProductHorizontalFragment)
}