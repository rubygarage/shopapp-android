package com.client.shop.ui.item

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.CardView
import android.view.View
import com.client.shop.R
import com.domain.entity.Product
import kotlinx.android.synthetic.main.item_product_grid.view.*

@SuppressLint("ViewConstructor")
class ProductItem(context: Context) : CardView(context) {

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.item_product_grid, this)
        useCompatPadding = true
        preventCornerOverlap = true
    }

    fun setProduct(product: Product) {
        title.text = product.title
        productDescription.text = product.productDescription
        price.text = context.getString(R.string.price_holder, product.price, product.currency)
        if (product.images.isNotEmpty()) {
            image.setImageURI(product.images[0].src)
        }
    }
}