package com.client.shop.ui.cart.di

import com.client.shop.ui.cart.CartActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(CartModule::class))
interface CartComponent {

    fun inject(activity: CartActivity)
}