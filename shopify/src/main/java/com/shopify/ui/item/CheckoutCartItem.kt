package com.shopify.ui.item

import android.content.Context
import android.view.ViewGroup
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.shopify.api.R

class CheckoutCartItem(context: Context) : SimpleDraweeView(context) {

    init {
        val width = resources.getDimensionPixelSize(R.dimen.checkout_cart_item_width)
        val height = resources.getDimensionPixelSize(R.dimen.checkout_cart_item_height)
        layoutParams = ViewGroup.LayoutParams(width, height)
        hierarchy.setPlaceholderImage(R.drawable.ic_product_placeholder, ScalingUtils.ScaleType.FIT_CENTER)
    }
}