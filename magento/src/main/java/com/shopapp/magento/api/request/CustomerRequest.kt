package com.shopapp.magento.api.request

import com.google.gson.annotations.SerializedName
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Customer

class CustomerRequest(val customer: CustomerData) {

    class CustomerData(customer: Customer) {

        val email: String
        val website_id: Int
        val addresses: List<AddressData>

        @SerializedName("firstname")
        val firstName: String

        @SerializedName("lastname")
        val lastName: String

        init {
            email = customer.email
            firstName = customer.firstName
            lastName = customer.lastName
            website_id = 0
            addresses = customer.addressList.map { AddressData(it, it == customer.defaultAddress) }
        }
    }

    class AddressData(address: Address, isDefault: Boolean = false) {

        val id: String?
        val countryId: String
        val street: List<String>
        val telephone: String
        val postcode: String
        val city: String
        val firstname: String
        val lastname: String
        val defaultShipping: Int

        @SerializedName("region_id")
        val regionId: String?

        init {
            val addresses = mutableListOf(address.address)
            address.secondAddress?.let {
                addresses.add(it)
            }

            id = if (address.id == Address.NO_ID) null else address.id
            regionId = address.state?.id
            countryId = address.country.id
            street = addresses
            telephone = address.phone ?: ""
            postcode = address.zip
            city = address.city
            firstname = address.firstName
            lastname = address.lastName
            defaultShipping = if (isDefault) 1 else 0
        }
    }
}