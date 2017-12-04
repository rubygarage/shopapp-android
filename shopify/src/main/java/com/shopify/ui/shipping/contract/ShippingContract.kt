package com.shopify.ui.shipping.contract

import com.domain.entity.Address
import com.domain.interactor.base.SingleUseCase
import com.shopify.entity.Checkout
import com.shopify.entity.ShippingRate
import com.shopify.repository.CheckoutRepository
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import io.reactivex.Single
import javax.inject.Inject

interface ShippingView : BaseLceView<List<ShippingRate>> {

    fun shippingRateSelected(checkout: Checkout)
}

class ShippingPresenter(
        private val getShippingRatesUseCase: GetShippingRatesUseCase,
        private val selectShippingRateUseCase: SelectShippingRateUseCase
) :
        BaseLcePresenter<List<ShippingRate>, ShippingView>(
                getShippingRatesUseCase,
                selectShippingRateUseCase
        ) {

    fun getShippingRates(checkoutId: String, email: String, address: Address) {

        getShippingRatesUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                GetShippingRatesUseCase.Params(checkoutId, email, address)
        )
    }

    fun selectShippingRate(checkoutId: String, shippingRate: ShippingRate) {

        selectShippingRateUseCase.execute(
                { view?.shippingRateSelected(it) },
                { resolveError(it) },
                SelectShippingRateUseCase.Params(checkoutId, shippingRate)
        )
    }
}

class GetShippingRatesUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
        SingleUseCase<List<ShippingRate>, GetShippingRatesUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<ShippingRate>> =
            with(params) {
                checkoutRepository.getShippingRates(checkoutId, email, address)
            }

    class Params(
            val checkoutId: String,
            val email: String,
            val address: Address
    )
}

class SelectShippingRateUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
        SingleUseCase<Checkout, SelectShippingRateUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Checkout> =
            with(params) {
                checkoutRepository.selectShippingRate(checkoutId, shippingRate)
            }

    class Params(
            val checkoutId: String,
            val shippingRate: ShippingRate
    )
}