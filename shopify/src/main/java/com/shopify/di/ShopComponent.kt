package com.shopify.di

import com.shopify.ui.checkout.di.CheckoutComponent
import com.shopify.ui.checkout.di.CheckoutModule
import com.shopify.ui.payment.android.di.AndroidPaymentComponent
import com.shopify.ui.payment.android.di.AndroidPaymentModule
import com.shopify.ui.payment.card.di.CardPaymentComponent
import com.shopify.ui.payment.card.di.CardPaymentModule
import com.shopify.ui.shipping.di.ShippingComponent
import com.shopify.ui.shipping.di.ShippingModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ShopifyModule::class))
interface ShopComponent {

    fun attachCheckoutComponent(module: CheckoutModule): CheckoutComponent

    fun attachCardPaymentComponent(module: CardPaymentModule): CardPaymentComponent

    fun attachAndroidPaymentComponent(module: AndroidPaymentModule): AndroidPaymentComponent

    fun attachShippingComponent(module: ShippingModule): ShippingComponent
}