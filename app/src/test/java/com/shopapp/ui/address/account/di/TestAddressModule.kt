package com.shopapp.ui.address.account.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.address.account.router.AddressListRouter
import com.shopapp.ui.address.base.contract.AddressListPresenter
import com.shopapp.ui.address.base.contract.AddressListView
import com.shopapp.ui.address.base.contract.AddressPresenter
import com.shopapp.ui.address.base.contract.AddressView
import dagger.Module
import dagger.Provides

@Module
class TestAddressModule {

    @Provides
    fun provideAddressPresenter(): AddressPresenter<AddressView> = mock()

    @Provides
    fun provideAddressListPresenter(): AddressListPresenter<AddressListView> = mock()

    @Provides
    fun provideAddressesRouter(): AddressListRouter = mock()
}