package com.shopify.ui.checkout.di

import com.domain.interactor.account.GetCustomerUseCase
import com.domain.interactor.cart.CartItemsUseCase
import com.shopify.interactor.checkout.CreateCheckoutUseCase
import com.shopify.interactor.checkout.GetCheckoutUseCase
import com.shopify.interactor.checkout.SetShippingAddressUseCase
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
        setShippingAddressUseCase: SetShippingAddressUseCase
    ): CheckoutPresenter = CheckoutPresenter(
        cartItemsUseCase,
        createCheckoutUseCase,
        getCheckoutUseCase,
        getCustomerUseCase,
        setShippingAddressUseCase
    )
}