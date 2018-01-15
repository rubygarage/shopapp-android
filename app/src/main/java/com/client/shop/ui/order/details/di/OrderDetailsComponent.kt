package com.client.shop.ui.details.di

import com.client.shop.ui.details.OrderDetailsActivity
import dagger.Subcomponent

@Subcomponent(modules = [OrderDetailsModule::class])
interface OrderDetailsComponent {

    fun inject(activity: OrderDetailsActivity)

}