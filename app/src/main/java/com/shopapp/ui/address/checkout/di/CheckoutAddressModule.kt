package com.shopapp.ui.address.checkout.di

import com.shopapp.domain.interactor.account.*
import com.shopapp.domain.interactor.checkout.SetShippingAddressUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.ui.address.base.contract.AddressPresenter
import com.shopapp.ui.address.base.contract.AddressView
import com.shopapp.ui.address.checkout.contract.CheckoutAddressListPresenter
import com.shopapp.ui.address.checkout.contract.CheckoutUnAuthAddressPresenter
import com.shopapp.ui.address.checkout.router.CheckoutAddressRouter
import dagger.Module
import dagger.Provides

@Module
class CheckoutAddressModule {

    @Provides
    fun provideCheckoutUnAuthAddressPresenter(
        formValidator: FieldValidator,
        countriesUseCase: GetCountriesUseCase,
        setShippingAddressUseCase: SetShippingAddressUseCase,
        addCustomerAddressUseCase: AddCustomerAddressUseCase,
        updateCustomerAddressUseCase: UpdateCustomerAddressUseCase
    ): CheckoutUnAuthAddressPresenter = CheckoutUnAuthAddressPresenter(
        formValidator,
        countriesUseCase,
        setShippingAddressUseCase,
        addCustomerAddressUseCase,
        updateCustomerAddressUseCase
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
        addCustomerAddressUseCase: AddCustomerAddressUseCase,
        updateCustomerAddressUseCase: UpdateCustomerAddressUseCase
    ): AddressPresenter<AddressView> =
        AddressPresenter(
            formValidator,
            countriesUseCase,
            addCustomerAddressUseCase,
            updateCustomerAddressUseCase
        )

    @Provides
    fun provideCheckoutAddressesRouter(): CheckoutAddressRouter = CheckoutAddressRouter()
}