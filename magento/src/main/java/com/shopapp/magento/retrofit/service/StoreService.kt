package com.shopapp.magento.retrofit.service

import com.shopapp.magento.api.response.StoreConfigResponse
import io.reactivex.Single
import retrofit2.http.GET

interface StoreService {

    companion object {
        const val STORE_CONFIGS_URL = "store/storeConfigs"
    }

    @GET(STORE_CONFIGS_URL)
    fun getStoreConfigs(): Single<StoreConfigResponse>
}