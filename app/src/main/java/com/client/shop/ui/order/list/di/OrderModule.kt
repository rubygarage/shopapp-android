package com.client.shop.ui.order.list.di

import com.client.shop.ui.order.list.contract.OrderListPresenter
import com.domain.interactor.order.OrderListUseCase
import dagger.Module
import dagger.Provides

@Module
class OrderModule {

    @Provides
    fun provideOrderListPresenter(orderListUseCase: OrderListUseCase): OrderListPresenter {
        return OrderListPresenter(orderListUseCase)
    }

}