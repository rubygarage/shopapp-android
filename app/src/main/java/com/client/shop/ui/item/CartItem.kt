package com.client.shop.ui.item

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import com.client.shop.R
import com.client.shop.ui.custom.SimpleTextWatcher
import com.domain.entity.CartProduct
import kotlinx.android.synthetic.main.item_cart.view.*

class CartItem @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) :
        CardView(context, attrs, defStyleAttr),
        SimpleTextWatcher,
        View.OnFocusChangeListener {

    var actionListener: ActionListener? = null
    private lateinit var cartProduct: CartProduct

    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        useCompatPadding = true
        preventCornerOverlap = true
        View.inflate(context, R.layout.item_cart, this)
        removeButton.setOnClickListener { actionListener?.onRemoveButtonClicked(cartProduct.productVariant.id) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        quantityEditText.addTextChangedListener(this)
        quantityEditText.onFocusChangeListener = this
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        quantityEditText.removeTextChangedListener(this)
        quantityEditText.onFocusChangeListener = null
    }

    fun setCartProduct(cartProduct: CartProduct) {
        this.cartProduct = cartProduct
        val product = cartProduct.productVariant

        productImage.setImageURI(product.image?.src)
        title.text = product.title
        quantityEditText.setText(cartProduct.quantity.toString())
        quantityEditText.setSelection(quantityEditText.text.length)
        totalPrice.text = getTotalPrice(product.price, cartProduct.quantity)
        changeEachPriceVisibility(product.price, cartProduct.quantity)
    }

    private fun getTotalPrice(priceStr: String, quantity: Int): String {
        val price = priceStr.toFloatOrNull() ?: 0f
        return context.getString(R.string.price_pattern, price * quantity, cartProduct.currency)
    }

    private fun changeEachPriceVisibility(priceStr: String, quantity: Int) {
        if (quantity > 1) {
            eachPrice.text = context.getString(R.string.each_price_pattern, priceStr, cartProduct.currency)
            eachPrice.visibility = View.VISIBLE
        } else {
            eachPrice.visibility = View.GONE
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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