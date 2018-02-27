package com.shopapp.ui.product.view

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.View
import com.shopapp.R
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.gateway.entity.VariantOption


class OptionsGroupContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayoutCompat(context, attrs, defStyleAttr) {

    companion object {
        const val ANIMATION_DURATION = 500L
    }

    var variantSelectListener: OnVariantSelectListener? = null

    init {
        orientation = VERTICAL
        View.inflate(context, R.layout.container_options_group, this)
    }

    fun setProduct(product: Product, preselectedVariant: ProductVariant?) {

        visibility = if (product.options.isEmpty() ||
            (product.options.size == 1 && product.options.first().values.size == 1)) {
            View.GONE
        } else {
            alpha = 0f
            animate().alpha(1f).duration = ANIMATION_DURATION
            View.VISIBLE
        }

        for (productOption in product.options) {
            val optionItem = OptionsContainer(context)
            val preselectedOption = preselectedVariant?.selectedOptions?.find {
                it.name == productOption.name
            }
            optionItem.setProductOption(productOption, preselectedOption)
            optionItem.onSelectOptionListener = object : OptionsContainer.OnSelectOptionListener {
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
            .map { getChildAt(it) as? OptionsContainer }
            .map {
                it?.getSelectedVariantOption()?.let {
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