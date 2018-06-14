package com.shopapp.domain.repository

import com.shopapp.gateway.entity.Order
import io.reactivex.Single

interface OrderRepository {

    fun getOrders(perPage: Int, paginationValue: Any?): Single<List<Order>>

    fun getOrder(id: String): Single<Order>
}