package com.shopify.ui.shipping.di

import com.shopify.ui.shipping.contract.GetShippingRatesUseCase
import com.shopify.ui.shipping.contract.SelectShippingRateUseCase
import com.shopify.ui.shipping.contract.ShippingPresenter
import dagger.Module
import dagger.Provides

@Module
class ShippingModule {

    @Provides
    fun provideShippingPresenter(getShippingRatesUseCase: GetShippingRatesUseCase,
                                 selectShippingRateUseCase: SelectShippingRateUseCase): ShippingPresenter =
            ShippingPresenter(getShippingRatesUseCase, selectShippingRateUseCase)
}