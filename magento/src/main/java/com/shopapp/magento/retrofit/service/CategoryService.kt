package com.shopapp.magento.retrofit.service

import com.shopapp.magento.api.response.CategoryDetailsResponse
import com.shopapp.magento.api.response.CategoryListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryService {

    @GET("categories")
    fun getCategoryList(@Query("rootCategoryId") rootCategoryId: String?): Single<CategoryListResponse>

    @GET("categories/{categoryId}")
    fun getCategoryDetails(@Path("categoryId") categoryId: String): Single<CategoryDetailsResponse>
}