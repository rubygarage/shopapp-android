package com.client.shop.ui.item

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.domain.entity.OrderProduct
import com.domain.formatter.NumberFormatter
import kotlinx.android.synthetic.main.item_cart.view.*

@SuppressLint("ViewConstructor")
class OrderProductItem constructor(context: Context, private val formatter: NumberFormatter)
    : ConstraintLayout(context) {

    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.item_order_product, this)
        layoutTransition = LayoutTransition()
    }

    fun setOrderProduct(orderProduct: OrderProduct, currency: String) {

        val productVariant = orderProduct.productVariant

        val imageURI: String? = productVariant.image?.src ?: productVariant.productImage?.src

        productImage.setImageURI(imageURI)
        titleText.text = productVariant.title
        quantityEditText.setText(orderProduct.quantity.toString())
        quantityEditText.setSelection(quantityEditText.text.length)
        totalPrice.text = getTotalPrice(productVariant.price, orderProduct.quantity, currency)
    }

    private fun getTotalPrice(price: Float, quantity: Int, currency: String): String {
        return formatter.formatPrice(price * quantity, currency)
    }
}