package com.shopify.di

import com.shopify.ui.CheckoutActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(CheckoutModule::class))
interface ShopComponent {

    fun inject(activity: CheckoutActivity)
}