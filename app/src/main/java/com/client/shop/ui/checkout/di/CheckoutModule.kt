package com.client.shop.ui.checkout.di

import com.client.shop.ui.checkout.contract.CheckoutPresenter
import com.domain.interactor.account.GetCustomerUseCase
import com.domain.interactor.cart.CartItemsUseCase
import com.domain.interactor.cart.CartRemoveAllUseCase
import com.domain.interactor.checkout.*
import dagger.Module
import dagger.Provides

@Module
class CheckoutModule {

    @Provides
    fun provideCheckoutPresenter(
        cartItemsUseCase: CartItemsUseCase,
        cartRemoveAllUseCase: CartRemoveAllUseCase,
        createCheckoutUseCase: CreateCheckoutUseCase,
        getCheckoutUseCase: GetCheckoutUseCase,
        getCustomerUseCase: GetCustomerUseCase,
        setShippingAddressUseCase: SetShippingAddressUseCase,
        getShippingRatesUseCase: GetShippingRatesUseCase,
        setShippingRateUseCase: SetShippingRateUseCase,
        completeCheckoutByCardUseCase: CompleteCheckoutByCardUseCase
    ): CheckoutPresenter = CheckoutPresenter(
        cartItemsUseCase,
        cartRemoveAllUseCase,
        createCheckoutUseCase,
        getCheckoutUseCase,
        getCustomerUseCase,
        setShippingAddressUseCase,
        getShippingRatesUseCase,
        setShippingRateUseCase,
        completeCheckoutByCardUseCase
    )
}