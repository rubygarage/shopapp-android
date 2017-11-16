package com.repository.impl

import com.domain.entity.CartProduct
import com.domain.network.Api
import com.repository.CheckoutRepository
import com.repository.rx.RxCallback
import io.reactivex.Single

class CheckoutRepositoryImpl(private val api: Api) : CheckoutRepository {

    override fun createCheckout(cartProductList: List<CartProduct>): Single<Pair<String, String>> {
        return Single.create<Pair<String, String>> { emitter ->
            api.createCheckout(cartProductList, RxCallback<Pair<String, String>>(emitter))
        }
    }
}