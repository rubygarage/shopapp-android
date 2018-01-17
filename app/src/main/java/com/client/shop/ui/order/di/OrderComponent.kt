package com.client.shop.ui.order.di

import com.client.shop.ui.details.OrderDetailsActivity
import com.client.shop.ui.order.list.OrderListActivity
import dagger.Subcomponent

@Subcomponent(modules = [OrderModule::class])
interface OrderComponent {

    fun inject(orderListActivity: OrderListActivity)

    fun inject(orderDetailsActivity: OrderDetailsActivity)

}