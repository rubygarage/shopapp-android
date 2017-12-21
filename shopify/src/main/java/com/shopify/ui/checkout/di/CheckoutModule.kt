package com.shopify.ui.checkout.di

import com.domain.interactor.cart.CartItemsUseCase
import com.shopify.interactor.checkout.CheckoutUseCase
import com.shopify.ui.checkout.contract.CheckoutPresenter
import dagger.Module
import dagger.Provides

@Module
class CheckoutModule {

    @Provides
    fun provideCheckoutPresenter(cartItemsUseCase: CartItemsUseCase, checkoutUseCase: CheckoutUseCase): CheckoutPresenter =
            CheckoutPresenter(cartItemsUseCase, checkoutUseCase)
}