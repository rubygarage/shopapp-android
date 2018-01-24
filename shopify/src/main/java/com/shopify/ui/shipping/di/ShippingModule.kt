package com.shopify.ui.shipping.di

import com.shopify.ui.shipping.contract.ShippingPresenter
import dagger.Module
import dagger.Provides

@Module
class ShippingModule {

    @Provides
    fun provideShippingPresenter(): ShippingPresenter = ShippingPresenter()
}