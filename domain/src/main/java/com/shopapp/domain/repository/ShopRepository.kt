package com.shopapp.domain.repository

import com.shopapp.gateway.entity.Config
import com.shopapp.gateway.entity.Shop
import io.reactivex.Single

interface ShopRepository {

    fun getShop(): Single<Shop>

    fun getConfig(): Single<Config>
}