package com.client.shop.ui.item

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import com.client.shop.R
import com.domain.entity.Product
import com.domain.formatter.NumberFormatter
import kotlinx.android.synthetic.main.item_recent.view.*

@SuppressLint("ViewConstructor")
class ProductItem(
    context: Context,
    viewWidth: Int,
    viewHeight: Int,
    private val formatter: NumberFormatter
) : ConstraintLayout(context) {

    init {
        View.inflate(context, R.layout.item_recent, this)
        layoutParams = LayoutParams(viewWidth, viewHeight)
    }

    fun setProduct(product: Product) {
        titleTextView.text = product.title
        price.text = formatter.formatPrice(product.price, product.currency)
        image.setImageURI(product.images.firstOrNull()?.src)
    }
}