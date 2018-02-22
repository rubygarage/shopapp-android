package com.shopapp.ui.item

import android.content.Context
import android.view.ViewGroup
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.view.SimpleDraweeView
import com.shopapp.R

class ProductVariantItem(context: Context) : SimpleDraweeView(context) {

    init {
        val width = resources.getDimensionPixelSize(R.dimen.product_variant_item_width)
        val height = resources.getDimensionPixelSize(R.dimen.product_variant_item_height)
        layoutParams = ViewGroup.LayoutParams(width, height)
        hierarchy.setPlaceholderImage(R.drawable.ic_placeholder, ScalingUtils.ScaleType.CENTER_INSIDE)
        hierarchy.actualImageScaleType = ScalingUtils.ScaleType.FIT_CENTER
    }

}