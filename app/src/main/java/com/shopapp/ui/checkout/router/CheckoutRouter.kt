package com.shopapp.ui.checkout.router

import android.app.Activity
import android.content.Context
import android.support.v4.app.TaskStackBuilder
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Card
import com.shopapp.ui.address.checkout.CheckoutAddressListActivity
import com.shopapp.ui.address.checkout.CheckoutUnAuthAddressActivity
import com.shopapp.ui.checkout.payment.PaymentActivity
import com.shopapp.ui.checkout.payment.card.CardActivity
import com.shopapp.ui.const.PaymentType
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.order.success.OrderSuccessActivity

class CheckoutRouter {

    fun showPaymentResult(activity: Activity, paymentType: PaymentType?, requestCode: Int) {
        activity.startActivityForResult(PaymentActivity.getStartIntent(activity, paymentType), requestCode)
    }

    fun showCardForResult(activity: Activity, card: Card?, requestCode: Int) {
        activity.startActivityForResult(CardActivity.getStartIntent(activity, card), requestCode)
    }

    fun showCheckoutAddressListForResult(activity: Activity,
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

    fun showCheckoutUnAuthAddressListForResult(activity: Activity,
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

    fun showHome(context: Context?, isNewTask: Boolean = false) {
        context?.let { it.startActivity(HomeActivity.getStartIntent(it, isNewTask)) }
    }

    fun showSuccessOrder(context: Context?, id: String, orderNumber: Int) {
        context?.let {
            val taskBuilder = TaskStackBuilder.create(it)
            taskBuilder.addNextIntent(HomeActivity.getStartIntent(it, true))
            taskBuilder.addNextIntent(OrderSuccessActivity.getStartIntent(it, id, orderNumber))
            taskBuilder.startActivities()
        }
    }
}