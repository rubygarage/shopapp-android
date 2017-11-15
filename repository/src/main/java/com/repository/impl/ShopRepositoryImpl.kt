package com.repository.impl

import com.domain.entity.Shop
import com.repository.rx.RxCallback
import com.repository.ShopRepository
import com.domain.network.Api
import io.reactivex.Single

class ShopRepositoryImpl(private val api: Api) : ShopRepository {

    override fun getShop(): Single<Shop> {
        return Single.create<Shop> { emitter ->
            api.getShopInfo(RxCallback<Shop>(emitter))
        }
    }
}