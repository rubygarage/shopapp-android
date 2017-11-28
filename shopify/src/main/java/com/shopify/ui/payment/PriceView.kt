package com.shopify.ui.payment

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.shopify.api.R
import com.shopify.entity.Checkout
import kotlinx.android.synthetic.main.view_price.view.*

class PriceView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_price, this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    fun setCheckout(checkout: Checkout) {
        priceValue.text = String.format(checkout.subtotalPrice.toString())
        deliveryPriceValue.text = String.format((checkout.totalPrice - checkout.subtotalPrice).toString())
        taxValue.text = String.format(checkout.taxPrice.toString())
        totalValue.text = String.format(checkout.totalPrice.toString())
    }
}