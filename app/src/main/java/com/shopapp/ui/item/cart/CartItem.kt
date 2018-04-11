package com.shopapp.ui.item.cart

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.shopapp.R
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.ui.view.QuantityView
import kotlinx.android.synthetic.main.item_cart.view.*
import java.math.BigDecimal

@SuppressLint("ViewConstructor")
class CartItem constructor(context: Context, private val formatter: NumberFormatter) : ConstraintLayout(context),
    QuantityView.OnQuantityChangeListener {

    var actionListener: ActionListener? = null
    private lateinit var cartProduct: CartProduct

    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        View.inflate(context, R.layout.item_cart, this)
        layoutTransition = LayoutTransition()
    }

    fun setCartProduct(cartProduct: CartProduct) {
        this.cartProduct = cartProduct
        val product = cartProduct.productVariant
        val imageURI: String? = product.image?.src ?: product.productImage?.src

        productImage.setImageURI(imageURI)
        titleText.text =
                resources.getString(R.string.cart_product_title, cartProduct.title, product.title)
        quantityView.text = cartProduct.quantity.toString()
        quantityView.quantityChangeListener = this
        totalPrice.text = getTotalPrice(product.price, cartProduct.quantity)
        changeEachPriceVisibility(product.price, cartProduct.quantity)
    }

    private fun getTotalPrice(price: BigDecimal, quantity: Int): String {
        return formatter.formatPrice(price * quantity.toBigDecimal(), cartProduct.currency)
    }

    private fun changeEachPriceVisibility(price: BigDecimal, quantity: Int) {
        if (quantity > 1) {
            eachPrice.text = context.getString(R.string.each_price_pattern,
                formatter.formatPrice(price, cartProduct.currency))
            eachPrice.visibility = View.VISIBLE
        } else {
            eachPrice.visibility = View.GONE
        }
    }

    override fun onQuantityChanged(quantity: String) {
        val quantityNumber = quantity.toIntOrNull() ?: 0
        if (quantityNumber > 0 && quantityNumber != cartProduct.quantity) {
            totalPrice.text = getTotalPrice(cartProduct.productVariant.price, quantityNumber)
            actionListener?.onQuantityChanged(cartProduct.productVariant.id, quantityNumber)
            changeEachPriceVisibility(cartProduct.productVariant.price, cartProduct.quantity)
        }
    }

    interface ActionListener {

        fun onRemoveButtonClicked(productVariantId: String)

        fun onQuantityChanged(productVariantId: String, newQuantity: Int)
    }
}