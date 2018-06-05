package com.shopapp.magento.api.request

import com.google.gson.annotations.SerializedName

class UpdateCustomerRequest(val customer: CustomerData) {

    class CustomerData(
        val email: String,
        @SerializedName("firstname")
        val firstName: String,
        @SerializedName("lastname")
        val lastName: String,
        val website_id: Int,
        val addresses: List<AddressData>
    )

    class AddressData()
}