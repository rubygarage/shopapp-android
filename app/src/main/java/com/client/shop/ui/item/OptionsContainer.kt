package com.client.shop.ui.item

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.View
import com.shopapicore.entity.Product
import com.shopapicore.entity.ProductVariant
import com.shopapicore.entity.VariantOption


class OptionsContainer @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleAttr: Int = 0) :
        LinearLayoutCompat(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }

    companion object {
        const val ANIMATION_DURATION = 500L
    }

    var variantSelectListener: OnVariantSelectListener? = null

    fun setProduct(product: Product) {

        if (product.options.isEmpty() || (product.options.size == 1 && product.options[0].values.size == 1)) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            alpha = 0f
            animate().alpha(1f).duration = ANIMATION_DURATION
        }

        visibility = if (product.options.isEmpty() ||
                (product.options.size == 1 && product.options[0].values.size == 1))
            View.GONE
        else
            View.VISIBLE

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