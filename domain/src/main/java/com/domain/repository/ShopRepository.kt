package com.domain.repository

import com.client.shop.getaway.entity.Shop
import io.reactivex.Single

interface ShopRepository {

    fun getShop(): Single<Shop>
}