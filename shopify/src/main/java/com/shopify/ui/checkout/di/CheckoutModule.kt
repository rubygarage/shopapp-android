package com.shopify.ui.checkout.di

import com.shopify.ui.checkout.contract.CheckoutPresenter
import com.shopify.ui.checkout.contract.CheckoutUseCase
import dagger.Module
import dagger.Provides

@Module
class CheckoutModule {

    @Provides
    fun provideBlogPresenter(checkoutUseCase: CheckoutUseCase): CheckoutPresenter =
            CheckoutPresenter(checkoutUseCase)
}