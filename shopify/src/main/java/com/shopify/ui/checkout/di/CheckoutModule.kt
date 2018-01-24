package com.shopify.ui.checkout.di

import com.domain.interactor.account.GetCustomerUseCase
import com.domain.interactor.cart.CartItemsUseCase
import com.shopify.interactor.checkout.*
import com.shopify.ui.checkout.contract.CheckoutPresenter
import dagger.Module
import dagger.Provides

@Module
class CheckoutModule {

    @Provides
    fun provideCheckoutPresenter(
        cartItemsUseCase: CartItemsUseCase,
        createCheckoutUseCase: CreateCheckoutUseCase,
        getCheckoutUseCase: GetCheckoutUseCase,
        getCustomerUseCase: GetCustomerUseCase,
        setShippingAddressUseCase: SetShippingAddressUseCase,
        getShippingRatesUseCase: GetShippingRatesUseCase,
        setShippingRateUseCase: SetShippingRateUseCase
    ): CheckoutPresenter = CheckoutPresenter(
        cartItemsUseCase,
        createCheckoutUseCase,
        getCheckoutUseCase,
        getCustomerUseCase,
        setShippingAddressUseCase,
        getShippingRatesUseCase,
        setShippingRateUseCase
    )
}