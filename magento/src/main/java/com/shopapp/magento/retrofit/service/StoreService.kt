package com.shopapp.magento.retrofit.service

import com.shopapp.magento.api.response.StoreConfigResponse
import io.reactivex.Single
import retrofit2.http.GET

interface StoreService {

    @GET("store/storeConfigs")
    fun getStoreConfigs(): Single<StoreConfigResponse>
}