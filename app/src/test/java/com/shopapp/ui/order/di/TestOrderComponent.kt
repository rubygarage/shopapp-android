package com.shopapp.ui.order.di

import com.shopapp.ui.order.details.OrderDetailsActivity
import com.shopapp.ui.order.list.OrderListActivity
import dagger.Subcomponent

@Subcomponent(modules = [TestOrderModule::class])
interface TestOrderComponent : OrderComponent {

    override fun inject(activity: OrderListActivity)

    override fun inject(activity: OrderDetailsActivity)
}