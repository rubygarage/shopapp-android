package com.shopify.api.adapter

import com.domain.entity.Customer
import com.shopify.buy3.Storefront

object CustomerAdapter {

    fun adapt(adaptee: Storefront.Customer): Customer {
        return Customer(
                adaptee.id.toString(),
                adaptee.email,
                adaptee.firstName,
                adaptee.lastName
        )
    }
}