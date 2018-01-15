package com.client.shop.ui.details.contract

import com.client.shop.R
import com.domain.entity.CartProduct
import com.domain.entity.Order
import com.domain.entity.Product
import com.domain.entity.ProductVariant
import com.domain.interactor.details.DetailsCartUseCase
import com.domain.interactor.details.DetailsProductUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface OrderDetailsView : BaseLceView<Order>

class OrderDetailsPresenter @Inject constructor(
        private val detailsOrderUseCase: DetailsOrderUseCase
) : BaseLcePresenter<Order, OrderDetailsView>(detailsOrderUseCase) {

    fun loadOrderDetails(orderId: String) {

        detailsOrderUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                orderId
        )
    }

}