package com.shopify.ui.payment

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.domain.formatter.NumberFormatter
import com.shopify.api.R
import com.shopify.entity.Checkout
import kotlinx.android.synthetic.main.view_price.view.*

class PriceView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val formatter = NumberFormatter()

    init {
        View.inflate(context, R.layout.view_price, this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    fun setCheckout(checkout: Checkout) {
        with(checkout) {
            priceValue.text = formatter.formatPrice(subtotalPrice, currency)
            deliveryPriceValue.text = formatter.formatPrice(totalPrice - subtotalPrice, currency)
            taxValue.text = formatter.formatPrice(taxPrice, currency)
            totalValue.text = formatter.formatPrice(totalPrice, currency)
        }
    }
}