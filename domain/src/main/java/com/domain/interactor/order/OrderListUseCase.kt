package com.domain.interactor.order

import com.client.shop.gateway.entity.Order
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.OrderRepository
import io.reactivex.Single
import javax.inject.Inject

class OrderListUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    SingleUseCase<List<Order>, OrderListUseCase.Params>() {

    override fun buildUseCaseSingle(params: OrderListUseCase.Params): Single<List<Order>> {
        return orderRepository.getOrdersList(params.perPage, params.paginationValue)
    }

    data class Params(
        val perPage: Int,
        val paginationValue: String?
    )
}
