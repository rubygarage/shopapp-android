package com.shopify.ui.checkout.di

import com.domain.interactor.cart.CartItemsUseCase
import com.shopify.interactor.checkout.CreateCheckoutUseCase
import com.shopify.interactor.checkout.GetCheckoutUseCase
import com.shopify.ui.checkout.contract.CheckoutPresenter
import dagger.Module
import dagger.Provides

@Module
class CheckoutModule {

    @Provides
    fun provideCheckoutPresenter(
            cartItemsUseCase: CartItemsUseCase,
            createCheckoutUseCase: CreateCheckoutUseCase,
            getCheckoutUseCase: GetCheckoutUseCase
    ): CheckoutPresenter = CheckoutPresenter(cartItemsUseCase, createCheckoutUseCase, getCheckoutUseCase)
}