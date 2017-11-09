package com.daocore.adapter

import com.daocore.entity.CustomerData
import com.daocore.entity.CustomerDataEntity
import com.domain.entity.Customer

class CustomerAdapter {

    companion object {

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
}