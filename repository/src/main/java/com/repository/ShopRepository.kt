package com.repository

import com.domain.entity.Shop
import io.reactivex.Single

interface ShopRepository {

    fun getShop(): Single<Shop>
}