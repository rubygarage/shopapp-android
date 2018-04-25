package com.shopapp.magento.retrofit.service

import com.shopapp.magento.api.response.ProductListResponse
import com.shopapp.magento.api.response.ProductResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ProductService {

    @GET("products")
    fun getProductList(@QueryMap options: Map<String, String>): Single<ProductListResponse>

    @GET("products/{sku}")
    fun getProduct(@Path("sku") sku: String): Single<ProductResponse>
}