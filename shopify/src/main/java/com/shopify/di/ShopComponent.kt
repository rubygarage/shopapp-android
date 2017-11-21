package com.shopify.di

import com.shopify.ui.checkout.di.CheckoutComponent
import com.shopify.ui.checkout.di.CheckoutModule
import com.shopify.ui.payment.di.PaymentComponent
import com.shopify.ui.payment.di.PaymentModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ShopifyModule::class))
interface ShopComponent {

    fun attachCheckoutComponent(module: CheckoutModule): CheckoutComponent

    fun attachPaymentComponent(module: PaymentModule): PaymentComponent
}