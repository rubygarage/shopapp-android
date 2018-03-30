package com.shopapp.ui.address.account.di

import com.shopapp.domain.interactor.account.*
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.ui.address.account.router.AddressListRouter
import com.shopapp.ui.address.base.contract.AddressListPresenter
import com.shopapp.ui.address.base.contract.AddressListView
import com.shopapp.ui.address.base.contract.AddressPresenter
import com.shopapp.ui.address.base.contract.AddressView
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

    @Provides
    fun provideAddressesRouter(): AddressListRouter = AddressListRouter()
}