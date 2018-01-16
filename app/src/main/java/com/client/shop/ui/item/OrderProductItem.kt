package com.client.shop.ui.item

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.domain.entity.OrderProduct
import com.domain.formatter.NumberFormatter
import kotlinx.android.synthetic.main.item_order_product.view.*

@SuppressLint("ViewConstructor")
class OrderProductItem constructor(context: Context, private val formatter: NumberFormatter)
    : ConstraintLayout(context) {

    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.item_order_product, this)
        layoutTransition = LayoutTransition()
        setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    }

    fun setOrderProduct(orderProduct: OrderProduct, currency: String) {

        val productVariant = orderProduct.productVariant

        val imageURI: String? = productVariant.image?.src ?: productVariant.productImage?.src

        productImage.setImageURI(imageURI)
        titleText.text = orderProduct.title
        quantity.text = resources.getString(R.string.quantity_label_pattern, orderProduct.quantity)
        if (productVariant.selectedOptions.isNotEmpty()) {
            optionsTextView.visibility = View.VISIBLE
            optionsTextView.text = productVariant.selectedOptions
                    .joinToString(separator = System.lineSeparator()) {
                        resources.getString(R.string.selected_options_pattern, it.name, it.value)
                    }
        } else {
            optionsTextView.visibility = View.GONE
        }
        
        totalPrice.text = getTotalPrice(productVariant.price, orderProduct.quantity, currency)
        changeEachPriceVisibility(productVariant.price, orderProduct.quantity, currency)
    }

    private fun changeEachPriceVisibility(price: Float, quantity: Int, currency: String) {
        if (quantity > 1) {
            eachPrice.text = context.getString(R.string.each_price_pattern,
                    formatter.formatPrice(price, currency))
            eachPrice.visibility = View.VISIBLE
        } else {
            eachPrice.visibility = View.GONE
        }
    }


    private fun getTotalPrice(price: Float, quantity: Int, currency: String): String {
        return formatter.formatPrice(price * quantity, currency)
    }
}