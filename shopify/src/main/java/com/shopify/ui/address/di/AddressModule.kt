package com.shopify.ui.address.di

import com.domain.interactor.account.*
import com.domain.validator.FieldValidator
import com.shopify.interactor.checkout.SetShippingAddressUseCase
import com.shopify.ui.address.contract.CheckoutAddressListPresenter
import com.shopify.ui.address.contract.CheckoutUnAuthAddressPresenter
import com.ui.module.address.contract.AddressPresenter
import com.ui.module.address.contract.AddressView
import dagger.Module
import dagger.Provides

@Module
class AddressModule {

    @Provides
    fun provideCheckoutUnAuthAddressPresenter(
        formValidator: FieldValidator,
        setShippingAddressUseCase: SetShippingAddressUseCase,
        createCustomerAddressUseCase: CreateCustomerAddressUseCase,
        editCustomerAddressUseCase: EditCustomerAddressUseCase
    ): CheckoutUnAuthAddressPresenter = CheckoutUnAuthAddressPresenter(
        formValidator,
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
        createCustomerAddressUseCase: CreateCustomerAddressUseCase,
        editCustomerAddressUseCase: EditCustomerAddressUseCase
    ): AddressPresenter<AddressView> =
        AddressPresenter(
            formValidator,
            createCustomerAddressUseCase,
            editCustomerAddressUseCase
        )

}