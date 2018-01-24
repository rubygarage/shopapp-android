package com.shopify.api.adapter

import com.domain.entity.Customer
import com.shopify.buy3.Storefront
import com.ui.const.Constant.DEFAULT_STRING

object CustomerAdapter {

    fun adapt(adaptee: Storefront.Customer): Customer {
        return Customer(
            adaptee.id.toString(),
            adaptee.email,
            adaptee.firstName ?: DEFAULT_STRING,
            adaptee.lastName ?: DEFAULT_STRING,
            adaptee.phone ?: DEFAULT_STRING,
            AddressAdapter.adapt(adaptee.addresses),
            adaptee.defaultAddress?.let { AddressAdapter.adapt(it) }
        )
    }
}