package com.shopapp.domain.interactor.order

import com.shopapp.gateway.entity.Order
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.OrderRepository
import io.reactivex.Single
import javax.inject.Inject

class OrderListUseCase @Inject constructor(private val orderRepository: OrderRepository) :
    SingleUseCase<List<Order>, OrderListUseCase.Params>() {

    override fun buildUseCaseSingle(params: OrderListUseCase.Params): Single<List<Order>> {
        return orderRepository.getOrderList(params.perPage, params.paginationValue)
    }

    data class Params(
        val perPage: Int,
        val paginationValue: String?
    )
}
