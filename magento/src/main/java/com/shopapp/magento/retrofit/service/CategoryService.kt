package com.shopapp.magento.retrofit.service

import com.shopapp.magento.api.response.CategoryDetailsResponse
import com.shopapp.magento.api.response.CategoryListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryService {

    companion object {
        const val CATEGORY_LIST_URL = "categories"
        const val CATEGORY_DETAILS_URL = "categories/{categoryId}"
    }

    @GET(CATEGORY_LIST_URL)
    fun getCategoryList(@Query("rootCategoryId") parentCategoryId: String?): Single<CategoryListResponse>

    @GET(CATEGORY_DETAILS_URL)
    fun getCategoryDetails(@Path("categoryId") categoryId: String): Single<CategoryDetailsResponse>
}