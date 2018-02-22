package com.shopapp.ui.checkout.di

import com.shopapp.domain.interactor.account.GetCustomerUseCase
import com.shopapp.domain.interactor.cart.CartItemsUseCase
import com.shopapp.domain.interactor.cart.CartRemoveAllUseCase
import com.shopapp.domain.interactor.checkout.*
import com.shopapp.ui.checkout.contract.CheckoutPresenter
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