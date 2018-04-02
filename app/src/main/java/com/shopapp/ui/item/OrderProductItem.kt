package com.shopapp.ui.item

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import com.shopapp.R
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.gateway.entity.OrderProduct
import kotlinx.android.synthetic.main.item_order_product.view.*
import java.math.BigDecimal

@SuppressLint("ViewConstructor")
class OrderProductItem constructor(context: Context, private val formatter: NumberFormatter)
    : ConstraintLayout(context) {

    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.item_order_product, this)
        setBackgroundResource(R.color.white)
    }

    fun setOrderProduct(orderProduct: OrderProduct, currency: String) {

        val productVariant = orderProduct.productVariant
        val imageURI: String? = productVariant?.image?.src ?: productVariant?.productImage?.src

        productImage.setImageURI(imageURI)
        titleText.text = orderProduct.title
        quantity.text = context.getString(R.string.quantity_label_pattern, orderProduct.quantity)
        if (productVariant != null && productVariant.selectedOptions.isNotEmpty()) {
            optionsTextView.visibility = View.VISIBLE
            optionsTextView.text = productVariant.selectedOptions
                .joinToString(separator = System.lineSeparator()) {
                    context.getString(R.string.selected_options_pattern, it.name, it.value)
                }
        } else {
            optionsTextView.visibility = View.GONE
        }

        val price = productVariant?.price
        if (price != null) {
            totalPrice.text = getTotalPrice(price, orderProduct.quantity, currency)
            changeEachPriceVisibility(price, orderProduct.quantity, currency)
        } else {
            totalPrice.text = context.getString(R.string.n_a_placeholder)
            eachPrice.visibility = View.GONE
        }
    }

    private fun changeEachPriceVisibility(price: BigDecimal, quantity: Int, currency: String) {
        if (quantity > 1) {
            eachPrice.text = context.getString(R.string.each_price_pattern,
                formatter.formatPrice(price, currency))
            eachPrice.visibility = View.VISIBLE
        } else {
            eachPrice.visibility = View.GONE
        }
    }

    private fun getTotalPrice(price: BigDecimal, quantity: Int, currency: String): String {
        return formatter.formatPrice(price * quantity.toBigDecimal(), currency)
    }
}