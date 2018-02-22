package com.shopapp.ui.order.list.contract

import com.shopapp.gateway.entity.Order
import com.shopapp.domain.interactor.order.OrderListUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface OrderListView : BaseLceView<List<Order>>

class OrderListPresenter @Inject constructor(private val orderListUseCase: OrderListUseCase) :
    BaseLcePresenter<List<Order>, OrderListView>(orderListUseCase) {

    fun getOrders(perPage: Int, paginationValue: String?) {

        orderListUseCase.execute(
            {
                view?.showContent(it)
            },
            { resolveError(it) },
            OrderListUseCase.Params(perPage, paginationValue)
        )
    }
}