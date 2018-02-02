package com.shopify.api.retrofit

import com.google.gson.JsonObject
import com.shopify.api.entity.ApiCountryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface CountriesService {

    @GET("/admin/countries.json")
    fun recentDrives(): Call<ApiCountryResponse>

    @POST("/admin/oauth/fd719b8c3c31ea4ea5f4078e8b9a759f")
    fun authorize(): Call<JsonObject>
}