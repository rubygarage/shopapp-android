package com.domain.repository

import com.client.shop.getaway.entity.Order
import io.reactivex.Single

interface OrderRepository {

    fun getOrdersList(perPage: Int, paginationValue: Any?): Single<List<Order>>


    fun getOrder(orderId: String): Single<Order>

}