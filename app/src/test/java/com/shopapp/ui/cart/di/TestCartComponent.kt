package com.shopapp.ui.cart.di

import com.shopapp.ui.cart.CartActivity
import com.shopapp.ui.cart.CartWidget
import dagger.Subcomponent

@Subcomponent(modules = [TestCartModule::class])
interface TestCartComponent : CartComponent {

    override fun inject(activity: CartActivity)

    override fun inject(view: CartWidget)
}