package com.shopapp.ui.order.di

import com.shopapp.ui.order.details.OrderDetailsActivity
import com.shopapp.ui.order.list.OrderListActivity
import dagger.Subcomponent

@Subcomponent(modules = [OrderModule::class])
interface OrderComponent {

    fun inject(orderListActivity: OrderListActivity)

    fun inject(orderDetailsActivity: OrderDetailsActivity)
}