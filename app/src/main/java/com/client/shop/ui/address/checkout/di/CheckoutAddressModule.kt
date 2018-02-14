package com.client.shop.ui.address.checkout.di

import com.client.shop.ui.address.base.contract.AddressPresenter
import com.client.shop.ui.address.base.contract.AddressView
import com.client.shop.ui.address.checkout.contract.CheckoutAddressListPresenter
import com.client.shop.ui.address.checkout.contract.CheckoutUnAuthAddressPresenter
import com.domain.interactor.account.*
import com.domain.interactor.checkout.SetShippingAddressUseCase
import com.domain.validator.FieldValidator
import dagger.Module
import dagger.Provides

@Module
class CheckoutAddressModule {

    @Provides
    fun provideCheckoutUnAuthAddressPresenter(
        formValidator: FieldValidator,
        countriesUseCase: GetCountriesUseCase,
        setShippingAddressUseCase: SetShippingAddressUseCase,
        createCustomerAddressUseCase: CreateCustomerAddressUseCase,
        editCustomerAddressUseCase: EditCustomerAddressUseCase
    ): CheckoutUnAuthAddressPresenter = CheckoutUnAuthAddressPresenter(
        formValidator,
        countriesUseCase,
        setShippingAddressUseCase,
        createCustomerAddressUseCase,
        editCustomerAddressUseCase
    )

    @Provides
    fun provideCheckoutAddressListPresenter(
        getCustomerUseCase: GetCustomerUseCase,
        deleteCustomerAddressUseCase: DeleteCustomerAddressUseCase,
        setDefaultAddressUseCase: SetDefaultAddressUseCase,
        setShippingAddressUseCase: SetShippingAddressUseCase
    ): CheckoutAddressListPresenter {
        return CheckoutAddressListPresenter(
            getCustomerUseCase,
            deleteCustomerAddressUseCase,
            setDefaultAddressUseCase,
            setShippingAddressUseCase
        )
    }

    @Provides
    fun provideAddressPresenter(
        formValidator: FieldValidator,
        countriesUseCase: GetCountriesUseCase,
        createCustomerAddressUseCase: CreateCustomerAddressUseCase,
        editCustomerAddressUseCase: EditCustomerAddressUseCase
    ): AddressPresenter<AddressView> =
        AddressPresenter(
            formValidator,
            countriesUseCase,
            createCustomerAddressUseCase,
            editCustomerAddressUseCase
        )

}