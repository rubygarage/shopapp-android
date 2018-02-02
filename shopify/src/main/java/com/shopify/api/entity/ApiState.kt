package com.shopify.api.entity

import com.google.gson.annotations.SerializedName

data class ApiState(
    val id: Long,
    @SerializedName("country_id")
    val countryId: Long,
    val code: String,
    val name: String
)