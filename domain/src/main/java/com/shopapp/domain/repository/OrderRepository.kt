package com.shopapp.domain.repository

import com.shopapp.gateway.entity.Order
import io.reactivex.Single

interface OrderRepository {

    fun getOrderList(perPage: Int, paginationValue: Any?): Single<List<Order>>


    fun getOrder(orderId: String): Single<Order>

}