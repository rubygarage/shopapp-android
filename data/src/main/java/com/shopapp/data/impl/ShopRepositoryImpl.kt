package com.shopapp.data.impl

import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.Shop
import com.shopapp.domain.repository.ShopRepository
import com.shopapp.data.rx.RxCallbackSingle
import io.reactivex.Single

class ShopRepositoryImpl(private val api: Api) : ShopRepository {

    override fun getShop(): Single<Shop> {
        return Single.create<Shop> { api.getShopInfo(RxCallbackSingle<Shop>(it)) }
    }
}