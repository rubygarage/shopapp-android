package com.shopify.ui.shipping.contract

import com.domain.entity.Address
import com.shopify.entity.Checkout
import com.shopify.entity.ShippingRate
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView

interface ShippingView : BaseLceView<List<ShippingRate>> {

    fun shippingRateSelected(checkout: Checkout)
}

class ShippingPresenter :
    BaseLcePresenter<List<ShippingRate>, ShippingView>() {

    fun getShippingRates(checkoutId: String, email: String, address: Address) {


    }

    fun selectShippingRate(checkoutId: String, shippingRate: ShippingRate) {

    }
}