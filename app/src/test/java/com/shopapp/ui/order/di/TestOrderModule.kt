package com.shopapp.ui.order.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.order.details.contract.OrderDetailsPresenter
import com.shopapp.ui.order.details.router.OrderRouter
import com.shopapp.ui.order.list.contract.OrderListPresenter
import com.shopapp.ui.order.list.router.OrderListRouter
import com.shopapp.ui.order.success.router.OrderSuccessRouter
import dagger.Module
import dagger.Provides

@Module
class TestOrderModule {

    @Provides
    fun provideOrderListPresenter(): OrderListPresenter = mock()

    @Provides
    fun provideOrderDetailsPresenter(): OrderDetailsPresenter = mock()

    @Provides
    fun provideOrderRouter(): OrderRouter = mock()

    @Provides
    fun provideOrderListRouter(): OrderListRouter = mock()

    @Provides
    fun provideOrderSuccessRouter(): OrderSuccessRouter = mock()

}