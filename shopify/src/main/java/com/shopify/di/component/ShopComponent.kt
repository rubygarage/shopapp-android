package com.shopify.di.component

import com.shopify.di.module.RouterModule
import com.shopify.di.module.ShopifyModule
import com.shopify.ui.address.di.AddressComponent
import com.shopify.ui.address.di.AddressModule
import com.shopify.ui.checkout.di.CheckoutComponent
import com.shopify.ui.checkout.di.CheckoutModule
import com.shopify.ui.payment.card.di.CardPaymentComponent
import com.shopify.ui.payment.card.di.CardPaymentModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ShopifyModule::class, RouterModule::class])
interface ShopComponent {

    fun attachCheckoutComponent(module: CheckoutModule): CheckoutComponent

    fun attachCardPaymentComponent(module: CardPaymentModule): CardPaymentComponent

    fun attachAddressComponent(module: AddressModule): AddressComponent
}