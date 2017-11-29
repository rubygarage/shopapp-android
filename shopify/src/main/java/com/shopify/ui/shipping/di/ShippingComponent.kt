package com.shopify.ui.shipping.di

import com.shopify.ui.shipping.ShippingActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(ShippingModule::class))
interface ShippingComponent {

    fun inject(activity: ShippingActivity)
}