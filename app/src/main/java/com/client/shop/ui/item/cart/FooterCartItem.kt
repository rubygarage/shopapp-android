package com.client.shop.ui.item.cart

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.client.shop.R
import com.domain.entity.CartProduct
import kotlinx.android.synthetic.main.item_footer_cart.view.*

class FooterCartItem(context: Context?) : ConstraintLayout(context) {

    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        View.inflate(context, R.layout.item_footer_cart, this)
    }

    fun setData(data: List<CartProduct>) {

        val currency = data.getOrNull(0)?.currency ?: ""
        val totalPrice: Float = data
                .map { it.quantity * (it.productVariant.price.toFloatOrNull() ?: 0f) }
                .sum()

        orderTotalLabel.text = context.resources.getQuantityString(R.plurals.order_total_plurals, data.size, data.size)
        orderTotalValue.text = context.getString(R.string.price_pattern, totalPrice.toString(), currency)
    }

}