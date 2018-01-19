package com.client.shop.ui.details.contract

import com.domain.entity.Order
import com.domain.interactor.order.OrderDetailsUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
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