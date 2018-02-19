package com.domain.repository

import com.client.shop.gateway.entity.Shop
import io.reactivex.Single

interface ShopRepository {

    fun getShop(): Single<Shop>
}