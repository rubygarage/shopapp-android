package com.shopapp.data.impl

import com.shopapp.data.rx.RxCallbackSingle
import com.shopapp.domain.repository.OrderRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.Order
import io.reactivex.Single

class OrderRepositoryImpl(private val api: Api) : OrderRepository {

    override fun getOrders(perPage: Int, paginationValue: Any?): Single<List<Order>> {
        return Single.create<List<Order>> { emitter ->
            api.getOrders(perPage, paginationValue, RxCallbackSingle<List<Order>>(emitter))
        }
    }

    override fun getOrder(orderId: String): Single<Order> {
        return Single.create<Order> { emitter ->
            api.getOrder(orderId, RxCallbackSingle<Order>(emitter))
        }
    }
}