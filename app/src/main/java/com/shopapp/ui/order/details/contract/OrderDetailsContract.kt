package com.shopapp.ui.order.details.contract

import com.shopapp.gateway.entity.Order
import com.shopapp.domain.interactor.order.OrderDetailsUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
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