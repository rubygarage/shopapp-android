package com.shopapp.ui.checkout.di

import com.shopapp.domain.interactor.cart.CartRemoveAllUseCase
import com.shopapp.domain.interactor.checkout.*
import com.shopapp.ui.checkout.contract.CheckoutPresenter
import com.shopapp.ui.checkout.router.CheckoutRouter
import dagger.Module
import dagger.Provides

@Module
class CheckoutModule {

    @Provides
    fun provideCheckoutPresenter(
        setupCheckoutUseCase: SetupCheckoutUseCase,
        cartRemoveAllUseCase: CartRemoveAllUseCase,
        getCheckoutUseCase: GetCheckoutUseCase,
        getShippingRatesUseCase: GetShippingRatesUseCase,
        setShippingRateUseCase: SetShippingRateUseCase,
        completeCheckoutByCardUseCase: CompleteCheckoutByCardUseCase
    ): CheckoutPresenter = CheckoutPresenter(
        setupCheckoutUseCase,
        cartRemoveAllUseCase,
        getCheckoutUseCase,
        getShippingRatesUseCase,
        setShippingRateUseCase,
        completeCheckoutByCardUseCase
    )

    @Provides
    fun provideCheckoutRouter(): CheckoutRouter = CheckoutRouter()
}