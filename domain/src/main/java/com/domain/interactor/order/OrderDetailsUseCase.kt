package com.domain.interactor.order

import com.domain.entity.Order
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.OrderRepository
import io.reactivex.Single
import javax.inject.Inject

class OrderDetailsUseCase @Inject constructor(private val orderRepository: OrderRepository) :
        SingleUseCase<Order, String>() {

    override fun buildUseCaseSingle(params: String): Single<Order> {
        return orderRepository.getOrder(params)
    }

}

