package com.client.shop.ui.order.details.contract

import com.client.shop.gateway.entity.Order
import com.domain.interactor.order.OrderDetailsUseCase
import com.client.shop.ui.base.contract.BaseLcePresenter
import com.client.shop.ui.base.contract.BaseLceView
import javax.inject.Inject

interface OrderDetailsView : BaseLceView<Order>

class OrderDetailsPresenter @Inject constructor(
    private val orderDetailsUseCase: OrderDetailsUseCase
) : BaseLcePresenter<Order, OrderDetailsView>(orderDetailsUseCase) {

    fun loadOrderDetails(orderId: String) {

        orderDetailsUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            orderId
        )
    }

}