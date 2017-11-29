package com.data.dao.adapter

import com.domain.entity.Customer
import com.data.dao.entity.CustomerData
import com.data.dao.entity.CustomerDataEntity

object CustomerAdapter {

    fun adaptToStore(adaptee: Customer): CustomerData {
        val customer = CustomerDataEntity()
        customer.id = adaptee.id
        customer.email = adaptee.email
        customer.firstName = adaptee.firstName
        customer.lastName = adaptee.lastName
        return customer
    }

    fun adaptFromStore(adaptee: CustomerData): Customer {
        return Customer(
                adaptee.id,
                adaptee.email,
                adaptee.firstName,
                adaptee.lastName
        )
    }
}