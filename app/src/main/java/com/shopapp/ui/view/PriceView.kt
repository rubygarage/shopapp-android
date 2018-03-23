package com.shopapp.ui.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.shopapp.R
import com.shopapp.domain.formatter.NumberFormatter
import kotlinx.android.synthetic.main.view_price.view.*
import java.math.BigDecimal

class PriceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var formatter = NumberFormatter()

    init {
        View.inflate(context, R.layout.view_price, this)
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }

    fun setData(subtotalPrice: BigDecimal, discountPrice: BigDecimal, shippingPrice: BigDecimal,
                totalPrice: BigDecimal, currencyCode: String) {
        subtotalValue.text = formatter.formatPrice(subtotalPrice, currencyCode)
        discountValue.text = formatter.formatPrice(discountPrice, currencyCode)
        shippingValue.text = formatter.formatPrice(shippingPrice, currencyCode)
        totalValue.text = formatter.formatPrice(totalPrice, currencyCode)
    }
}