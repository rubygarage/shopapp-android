package com.shopapp.data.impl

import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.Order
import com.shopapp.domain.repository.OrderRepository
import com.shopapp.data.rx.RxCallbackSingle
import io.reactivex.Single

class OrderRepositoryImpl(private val api: Api) : OrderRepository {

    override fun getOrder(orderId: String): Single<Order> {
        return Single.create<Order> { emitter ->
            api.getOrder(orderId, RxCallbackSingle<Order>(emitter))
        }

    }

    override fun getOrderList(perPage: Int, paginationValue: Any?): Single<List<Order>> {
        return Single.create<List<Order>> { emitter ->
            api.getOrders(perPage, paginationValue, RxCallbackSingle<List<Order>>(emitter))
        }
    }
}