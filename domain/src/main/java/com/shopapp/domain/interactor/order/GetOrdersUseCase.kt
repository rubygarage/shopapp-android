package com.shopapp.domain.interactor.order

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.OrderRepository
import com.shopapp.gateway.entity.Order
import io.reactivex.Single
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(private val repository: OrderRepository) :
    SingleUseCase<List<Order>, GetOrdersUseCase.Params>() {

    override fun buildUseCaseSingle(params: GetOrdersUseCase.Params): Single<List<Order>> {
        return repository.getOrders(params.perPage, params.paginationValue)
    }

    data class Params(
        val perPage: Int,
        val paginationValue: String?
    )
}
