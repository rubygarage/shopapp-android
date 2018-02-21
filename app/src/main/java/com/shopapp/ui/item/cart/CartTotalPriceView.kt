package com.client.shop.ui.item.cart

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.client.shop.R
import com.client.shop.ext.sum
import com.client.shop.gateway.entity.CartProduct
import com.domain.formatter.NumberFormatter
import kotlinx.android.synthetic.main.item_footer_cart.view.*
import java.math.BigDecimal

class CartTotalPriceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val formatter: NumberFormatter

    init {
        View.inflate(context, R.layout.item_footer_cart, this)
        formatter = NumberFormatter()
        setBackgroundResource(R.color.colorBackgroundLight)
    }

    fun setData(data: List<CartProduct>) {

        val currency = data.getOrNull(0)?.currency ?: ""
        val totalPrice: BigDecimal = data
            .map { it.quantity.toBigDecimal() * it.productVariant.price }
            .sum()

        orderTotalLabel.text = context.resources.getQuantityString(R.plurals.order_total_plurals, data.size, data.size)
        orderTotalValue.text = formatter.formatPrice(totalPrice, currency)
    }
}