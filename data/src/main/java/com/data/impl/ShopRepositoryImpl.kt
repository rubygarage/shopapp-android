package com.data.impl

import com.data.rx.RxCallbackSingle
import com.domain.entity.Shop
import com.domain.network.Api
import com.domain.repository.ShopRepository
import io.reactivex.Single

class ShopRepositoryImpl(private val api: Api) : ShopRepository {

    override fun getShop(): Single<Shop> {
        return Single.create<Shop> { emitter ->
            api.getShopInfo(RxCallbackSingle<Shop>(emitter))
        }
    }
}