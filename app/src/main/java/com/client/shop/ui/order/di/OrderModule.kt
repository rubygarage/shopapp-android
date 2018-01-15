package com.client.shop.ui.order.di

import com.client.shop.ui.details.contract.OrderDetailsPresenter
import com.client.shop.ui.order.list.contract.OrderListPresenter
import com.domain.interactor.order.OrderDetailsUseCase
import com.domain.interactor.order.OrderListUseCase
import dagger.Module
import dagger.Provides

@Module
class OrderModule {

    @Provides
    fun provideOrderListPresenter(orderListUseCase: OrderListUseCase): OrderListPresenter {
        return OrderListPresenter(orderListUseCase)
    }

    @Provides
    fun provideOrderDetailsPresenter(orderDetailsUseCase: OrderDetailsUseCase): OrderDetailsPresenter {
        return OrderDetailsPresenter(orderDetailsUseCase)
    }
}