package com.shopapp.magento.api.response

import com.google.gson.annotations.SerializedName
import com.shopapp.gateway.entity.Customer

class CustomerResponse(
    val id: Int,
    val email: String,
    @SerializedName("firstname")
    val firstName: String,
    @SerializedName("lastname")
    val lastName: String
) {

    fun mapToEntity(): Customer {
        return Customer(
            id = id.toString(),
            email = email,
            firstName = firstName,
            lastName = lastName,
            phone = null,
            isAcceptsMarketing = false,
            addressList = listOf(),
            defaultAddress = null
        )
    }
}