package com.client.shop.ui.address.di

import com.domain.interactor.account.*
import com.domain.validator.FieldValidator
import com.ui.module.address.contract.AddressListPresenter
import com.ui.module.address.contract.AddressListView
import com.ui.module.address.contract.AddressPresenter
import com.ui.module.address.contract.AddressView
import dagger.Module
import dagger.Provides

@Module
class AddressModule {

    @Provides
    fun provideAddressPresenter(
        formValidator: FieldValidator,
        countriesUseCase: GetCountriesUseCase,
        createCustomerAddressUseCase: CreateCustomerAddressUseCase,
        editCustomerAddressUseCase: EditCustomerAddressUseCase
    ): AddressPresenter<AddressView> = AddressPresenter(
        formValidator,
        countriesUseCase,
        createCustomerAddressUseCase,
        editCustomerAddressUseCase
    )

    @Provides
    fun provideAddressListPresenter(
        getCustomerUseCase: GetCustomerUseCase,
        deleteCustomerAddressUseCase: DeleteCustomerAddressUseCase,
        setDefaultAddressUseCase: SetDefaultAddressUseCase
    ) = AddressListPresenter<AddressListView>(
        getCustomerUseCase,
        deleteCustomerAddressUseCase,
        setDefaultAddressUseCase
    )
}