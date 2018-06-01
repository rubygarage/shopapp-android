package com.shopapp.magento.retrofit.service

import com.shopapp.magento.api.response.ProductListResponse
import com.shopapp.magento.api.response.ProductResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ProductService {

    companion object {
        const val PRODUCT_LIST_URL = "products"
        const val PRODUCT_URL = "products/{sku}"
    }

    @GET(PRODUCT_LIST_URL)
    fun getProductList(@QueryMap options: Map<String, String>): Single<ProductListResponse>

    @GET(PRODUCT_URL)
    fun getProduct(@Path("sku") sku: String): Single<ProductResponse>
}