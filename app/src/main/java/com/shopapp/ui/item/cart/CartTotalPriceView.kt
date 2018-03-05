package com.shopapp.ui.item.cart

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.shopapp.R
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.ext.sum
import com.shopapp.gateway.entity.CartProduct
import kotlinx.android.synthetic.main.item_footer_cart.view.*
import java.math.BigDecimal

class CartTotalPriceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.item_footer_cart, this)
        setBackgroundResource(R.color.colorBackgroundLight)
    }

    fun setData(data: List<CartProduct>, formatter: NumberFormatter) {

        val currency = data.getOrNull(0)?.currency ?: ""
        val totalPrice: BigDecimal = data
            .map { it.quantity.toBigDecimal() * it.productVariant.price }
            .sum()

        orderTotalLabel.text = context.resources.getQuantityString(R.plurals.order_total_plurals, data.size, data.size)
        orderTotalValue.text = formatter.formatPrice(totalPrice, currency)
    }
}