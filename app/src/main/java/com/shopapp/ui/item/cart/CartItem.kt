package com.shopapp.ui.item.cart

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.R
import com.shopapp.ext.hideKeyboard
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.item_cart.view.*
import java.math.BigDecimal

@SuppressLint("ViewConstructor")
class CartItem constructor(context: Context, private val formatter: NumberFormatter) : ConstraintLayout(context),
    SimpleTextWatcher,
    View.OnFocusChangeListener {

    var actionListener: ActionListener? = null
    private lateinit var cartProduct: CartProduct

    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        View.inflate(context, R.layout.item_cart, this)
        layoutTransition = LayoutTransition()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        quantityEditText.addTextChangedListener(this)
        quantityEditText.setOnEditorActionListener { v, _, _ ->
            v.clearFocus()
            v.hideKeyboard()
            true
        }
        quantityEditText.onFocusChangeListener = this
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        quantityEditText.removeTextChangedListener(this)
        quantityEditText.onFocusChangeListener = null
        quantityEditText.hideKeyboard()
    }

    fun setCartProduct(cartProduct: CartProduct) {
        this.cartProduct = cartProduct
        val product = cartProduct.productVariant
        val imageURI: String? = product.image?.src ?: product.productImage?.src

        productImage.setImageURI(imageURI)
        titleText.text =
                resources.getString(R.string.cart_product_title, cartProduct.title, product.title)
        quantityEditText.setText(cartProduct.quantity.toString())
        quantityEditText.setSelection(quantityEditText.text.length)
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

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val quantity = s.toString().toIntOrNull() ?: 0
        if (quantity > 0 && quantity != cartProduct.quantity) {
            totalPrice.text = getTotalPrice(cartProduct.productVariant.price, quantity)
            actionListener?.onQuantityChanged(cartProduct.productVariant.id, quantity)
            changeEachPriceVisibility(cartProduct.productVariant.price, cartProduct.quantity)
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view is EditText && !hasFocus) {
            if (view.text.isEmpty() || view.text.toString() == "0") {
                view.setText(cartProduct.quantity.toString())
            }
        }
    }

    interface ActionListener {

        fun onRemoveButtonClicked(productVariantId: String)

        fun onQuantityChanged(productVariantId: String, newQuantity: Int)
    }
}