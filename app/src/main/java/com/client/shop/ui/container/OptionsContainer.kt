package com.client.shop.ui.container

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.View
import com.client.shop.R
import com.client.shop.ui.item.OptionsItem
import com.domain.entity.Product
import com.domain.entity.ProductVariant
import com.domain.entity.VariantOption


class OptionsContainer @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = 0) :
        LinearLayoutCompat(context, attrs, defStyleAttr) {

    companion object {
        const val ANIMATION_DURATION = 500L
    }

    var variantSelectListener: OnVariantSelectListener? = null

    init {
        orientation = VERTICAL
        View.inflate(context, R.layout.view_options_container, this)
    }

    fun setProduct(product: Product) {

        visibility = if (product.options.isEmpty() ||
                (product.options.size == 1 && product.options.first().values.size == 1)) {
            View.GONE
        } else {
            alpha = 0f
            animate().alpha(1f).duration = ANIMATION_DURATION
            View.VISIBLE
        }

        for (productOption in product.options) {
            val optionItem = OptionsItem(context)
            optionItem.setProductOption(productOption)
            optionItem.onSelectOptionListener = object : OptionsItem.OnSelectOptionListener {
                override fun onOptionsSelected() {
                    getSelectedVariant(product)
                }
            }
            addView(optionItem)
        }
        getSelectedVariant(product)
    }

    private fun getSelectedVariant(product: Product) {

        val selectedVariantOptions = mutableListOf<VariantOption>()
        (0 until childCount)
                .map { getChildAt(it) as? OptionsItem }
                .map {
                    it?.selectedVariantOptions?.let {
                        selectedVariantOptions.add(it)
                    }
                }
        val selectedVariant: ProductVariant? = product.variants.firstOrNull {
            it.selectedOptions.containsAll(selectedVariantOptions)
        }
        variantSelectListener?.onVariantSelected(selectedVariant)
    }

    interface OnVariantSelectListener {

        fun onVariantSelected(productVariant: ProductVariant?)
    }
}