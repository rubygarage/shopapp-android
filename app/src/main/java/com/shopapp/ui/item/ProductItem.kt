package com.client.shop.ui.item

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import com.client.shop.R
import com.client.shop.ext.setResizedImageUri
import com.client.shop.gateway.entity.Product
import com.domain.formatter.NumberFormatter
import kotlinx.android.synthetic.main.item_product.view.*

@SuppressLint("ViewConstructor")
class ProductItem(
    context: Context,
    viewWidth: Int,
    viewHeight: Int,
    private val formatter: NumberFormatter
) : ConstraintLayout(context) {

    init {
        View.inflate(context, R.layout.item_product, this)
        layoutParams = LayoutParams(viewWidth, viewHeight)
    }

    fun setProduct(product: Product) {
        titleTextView.text = product.title
        if (product.hasAlternativePrice) {
            price.text = resources.getString(R.string.range_price,
                formatter.formatPrice(product.price, product.currency))
        } else {
            price.text = formatter.formatPrice(product.price, product.currency)
        }
        image.setResizedImageUri(context, product.images.firstOrNull()?.src)
    }
}