package com.client.shop.ui.item.cart

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.client.shop.R
import com.domain.entity.CartProduct
import com.domain.formatter.NumberFormatter
import kotlinx.android.synthetic.main.item_footer_cart.view.*

@SuppressLint("ViewConstructor")
class FooterCartItem(context: Context, private val formatter: NumberFormatter) : ConstraintLayout(context) {

    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        View.inflate(context, R.layout.item_footer_cart, this)
    }

    fun setData(data: List<CartProduct>) {

        val currency = data.getOrNull(0)?.currency ?: ""
        val totalPrice: Float = data
                .map { it.quantity * it.productVariant.price }
                .sum()

        orderTotalLabel.text = context.resources.getQuantityString(R.plurals.order_total_plurals, data.size, data.size)
        orderTotalValue.text = formatter.formatPrice(totalPrice, currency)
    }
}