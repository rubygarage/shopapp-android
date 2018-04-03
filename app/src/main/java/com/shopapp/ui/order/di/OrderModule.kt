package com.shopapp.ui.order.di

import com.shopapp.domain.interactor.order.OrderDetailsUseCase
import com.shopapp.domain.interactor.order.OrderListUseCase
import com.shopapp.ui.order.details.contract.OrderDetailsPresenter
import com.shopapp.ui.order.details.router.OrderRouter
import com.shopapp.ui.order.list.contract.OrderListPresenter
import com.shopapp.ui.order.list.router.OrderListRouter
import com.shopapp.ui.order.success.router.OrderSuccessRouter
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

    @Provides
    fun provideOrderRouter(): OrderRouter = OrderRouter()

    @Provides
    fun provideOrderListRouter(): OrderListRouter = OrderListRouter()

    @Provides
    fun provideOrderSuccessRouter(): OrderSuccessRouter = OrderSuccessRouter()

}