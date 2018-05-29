package com.shopapp.magento.api.request

import com.google.gson.annotations.SerializedName

class SignUpRequest(val customer: CustomerData, val password: String) {

    class CustomerData(
        val email: String,
        @SerializedName("firstname") val firstName: String,
        @SerializedName("lastname") val lastName: String
    )
}