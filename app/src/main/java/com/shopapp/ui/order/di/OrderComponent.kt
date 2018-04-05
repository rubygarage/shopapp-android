package com.shopapp.ui.order.di

import com.shopapp.ui.order.details.OrderDetailsActivity
import com.shopapp.ui.order.list.OrderListActivity
import com.shopapp.ui.order.success.OrderSuccessActivity
import dagger.Subcomponent

@Subcomponent(modules = [OrderModule::class])
interface OrderComponent {

    fun inject(activity: OrderListActivity)

    fun inject(activity: OrderDetailsActivity)

    fun inject(activity: OrderSuccessActivity)
}