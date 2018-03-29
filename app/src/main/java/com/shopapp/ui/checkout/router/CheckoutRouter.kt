package com.shopapp.ui.checkout.router

import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Card
import com.shopapp.ui.address.checkout.CheckoutAddressListActivity
import com.shopapp.ui.address.checkout.CheckoutUnAuthAddressActivity
import com.shopapp.ui.checkout.CheckoutActivity
import com.shopapp.ui.checkout.payment.PaymentActivity
import com.shopapp.ui.checkout.payment.card.CardActivity
import com.shopapp.ui.const.PaymentType
import com.shopapp.ui.home.HomeActivity

class CheckoutRouter {

    fun showPaymentResult(activity: CheckoutActivity, paymentType: PaymentType?, requestCode: Int) {
        activity.startActivityForResult(PaymentActivity.getStartIntent(activity, paymentType), requestCode)
    }

    fun showCardForResult(activity: CheckoutActivity, card: Card?, requestCode: Int) {
        activity.startActivityForResult(CardActivity.getStartIntent(activity, card), requestCode)
    }

    fun showCheckoutAddressListForResult(activity: CheckoutActivity,
                                         checkoutId: String? = null,
                                         selectedAddress: Address? = null,
                                         isShippingAddress: Boolean,
                                         shippingAddress: Address?,
                                         billingAddress: Address?,
                                         requestCode: Int) {
        activity.startActivityForResult(CheckoutAddressListActivity.getStartIntent(
            context = activity,
            checkoutId = checkoutId,
            selectedAddress = selectedAddress,
            isShippingAddress = isShippingAddress,
            shippingAddress = shippingAddress,
            billingAddress = billingAddress
        ),
            requestCode
        )
    }

    fun showCheckoutUnAuthAddressListForResult(activity: CheckoutActivity,
                                               checkoutId: String? = null,
                                               selectedAddress: Address? = null,
                                               isShippingAddress: Boolean = false,
                                               requestCode: Int) {
        activity.startActivityForResult(CheckoutUnAuthAddressActivity.getStartIntent(
            context = activity,
            checkoutId = checkoutId,
            address = selectedAddress,
            isShipping = isShippingAddress
        ),
            requestCode
        )
    }

    fun showHome(activity: CheckoutActivity, isNewTask: Boolean = false) {
        activity.startActivity(HomeActivity.getStartIntent(activity, isNewTask))
    }
}