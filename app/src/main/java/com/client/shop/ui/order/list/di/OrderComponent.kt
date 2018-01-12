package com.client.shop.ui.order.list.di

import com.client.shop.ui.order.list.OrderListActivity
import com.client.shop.ui.search.di.SearchModule
import dagger.Subcomponent

@Subcomponent(modules = [OrderModule::class])
interface OrderComponent {

    fun inject(orderListActivity: OrderListActivity)

}