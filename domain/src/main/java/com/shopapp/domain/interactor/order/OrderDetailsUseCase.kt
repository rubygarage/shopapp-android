package com.shopapp.domain.interactor.order

import com.shopapp.gateway.entity.Order
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.OrderRepository
import io.reactivex.Single
import javax.inject.Inject

class OrderDetailsUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    SingleUseCase<Order, String>() {

    override fun buildUseCaseSingle(params: String): Single<Order> {
        return orderRepository.getOrder(params)
    }

}

