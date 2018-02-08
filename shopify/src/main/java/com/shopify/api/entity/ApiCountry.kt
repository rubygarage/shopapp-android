package com.shopify.api.entity

import com.google.gson.annotations.SerializedName

data class ApiCountry(
    val id: Long,
    val code: String,
    val name: String,
    @SerializedName("provinces")
    val states: List<ApiState>
)