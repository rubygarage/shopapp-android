package com.shopapp.ui.order.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.order.details.contract.OrderDetailsPresenter
import com.shopapp.ui.order.list.contract.OrderListPresenter
import dagger.Module
import dagger.Provides

@Module
class TestOrderModule {

    @Provides
    fun provideOrderListPresenter(): OrderListPresenter {
        return mock()
    }

    @Provides
    fun provideOrderDetailsPresenter(): OrderDetailsPresenter {
        return mock()
    }
}