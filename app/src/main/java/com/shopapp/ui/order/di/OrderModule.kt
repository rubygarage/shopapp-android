package com.shopapp.ui.order.di

import com.shopapp.domain.interactor.order.OrderDetailsUseCase
import com.shopapp.domain.interactor.order.OrderListUseCase
import com.shopapp.ui.order.details.contract.OrderDetailsPresenter
import com.shopapp.ui.order.list.contract.OrderListPresenter
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