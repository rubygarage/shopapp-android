package com.shopify.di

import com.shopify.ui.CheckoutActivity
import dagger.Subcomponent

@Subcomponent
interface ShopComponent {

    fun inject(activity: CheckoutActivity)
}