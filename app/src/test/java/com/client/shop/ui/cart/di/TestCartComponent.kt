package com.client.shop.ui.cart.di

import com.client.shop.ui.cart.CartActivity
import com.client.shop.ui.cart.CartWidget
import dagger.Subcomponent

@Subcomponent(modules = [TestCartModule::class])
interface TestCartComponent : CartComponent {

    override fun inject(activity: CartActivity)

    override fun inject(view: CartWidget)
}