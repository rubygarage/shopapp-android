package com.shopify.ui.address.di

import com.domain.interactor.account.CreateCustomerAddressUseCase
import com.domain.interactor.account.SessionCheckUseCase
import com.shopify.interactor.checkout.GetCheckoutUseCase
import com.shopify.interactor.checkout.SetShippingAddressUseCase
import com.shopify.ui.address.contract.AddressPresenter
import dagger.Module
import dagger.Provides

@Module
class AddressModule {

    @Provides
    fun provideAddressPresenter(
            getCheckoutUseCase: GetCheckoutUseCase,
            setShippingAddressUseCase: SetShippingAddressUseCase,
            sessionCheckUseCase: SessionCheckUseCase,
            createCustomerAddressUseCase: CreateCustomerAddressUseCase
    ): AddressPresenter = AddressPresenter(getCheckoutUseCase, setShippingAddressUseCase,
            sessionCheckUseCase, createCustomerAddressUseCase)
}