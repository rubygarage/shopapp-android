package com.client.shop.ui.order.list.di

import com.client.shop.ui.order.details.OrderDetailsActivity
import com.client.shop.ui.order.di.OrderComponent
import com.client.shop.ui.order.list.OrderListActivity
import dagger.Subcomponent

@Subcomponent(modules = [TestOrderModule::class])
interface TestOrderComponent : OrderComponent {

    override fun inject(orderListActivity: OrderListActivity)

    override fun inject(orderDetailsActivity: OrderDetailsActivity)
}