package com.data.impl

import com.domain.entity.Shop
import com.domain.network.Api
import com.domain.repository.ShopRepository
import com.data.rx.RxCallback
import io.reactivex.Single

class ShopRepositoryImpl(private val api: Api) : ShopRepository {

    override fun getShop(): Single<Shop> {
        return Single.create<Shop> { emitter ->
            api.getShopInfo(RxCallback<Shop>(emitter))
        }
    }
}