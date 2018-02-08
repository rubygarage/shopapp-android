package com.data.impl

import com.data.rx.RxCallbackSingle
import com.domain.entity.Order
import com.domain.network.Api
import com.domain.repository.OrderRepository
import io.reactivex.Single

class OrderRepositoryImpl(private val api: Api) : OrderRepository {

    override fun getOrder(orderId: String): Single<Order> {
        return Single.create<Order> { emitter ->
            api.getOrder(orderId, RxCallbackSingle<Order>(emitter))
        }

    }

    override fun getOrdersList(perPage: Int, paginationValue: Any?): Single<List<Order>> {
        return Single.create<List<Order>> { emitter ->
            api.getOrders(perPage, paginationValue, RxCallbackSingle<List<Order>>(emitter))
        }
    }
}