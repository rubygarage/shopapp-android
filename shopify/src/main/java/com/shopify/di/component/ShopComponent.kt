package com.shopify.di.component

import com.shopify.di.module.RouterModule
import com.shopify.di.module.ShopifyModule
import com.shopify.ui.address.di.AddressComponent
import com.shopify.ui.address.di.AddressModule
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
@Component(modules = [ShopifyModule::class, RouterModule::class])
interface ShopComponent {

    fun attachCheckoutComponent(module: CheckoutModule): CheckoutComponent

    fun attachCardPaymentComponent(module: CardPaymentModule): CardPaymentComponent

    fun attachAndroidPaymentComponent(module: AndroidPaymentModule): AndroidPaymentComponent

    fun attachShippingComponent(module: ShippingModule): ShippingComponent

    fun attachAddressComponent(module: AddressModule): AddressComponent
}