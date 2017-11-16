package com.shopify.di

import com.shopify.ui.checkout.di.CheckoutComponent
import com.shopify.ui.checkout.di.CheckoutModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ShopifyModule::class))
interface ShopComponent {

    fun attachCheckoutComponent(module: CheckoutModule): CheckoutComponent
}