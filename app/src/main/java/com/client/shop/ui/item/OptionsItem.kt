package com.client.shop.ui.item

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.client.shop.R
import com.google.android.flexbox.FlexboxLayout
import com.domain.entity.ProductOption
import com.domain.entity.VariantOption
import kotlinx.android.synthetic.main.item_options.view.*

class OptionsItem @JvmOverloads constructor(context: Context,
                                            attrs: AttributeSet? = null,
                                            defStyleAttr: Int = 0) :
        LinearLayoutCompat(context, attrs, defStyleAttr), View.OnClickListener {

    var selectedVariantOptions: VariantOption? = null
    private val selectedRes: Int
    private val unselectedRes: Int
    private var itemMargin: Int

    init {
        orientation = VERTICAL
        View.inflate(context, R.layout.item_options, this)
        selectedRes = R.drawable.background_selected_variant
        unselectedRes = R.drawable.background_unselected_variant
        itemMargin = context.resources.getDimensionPixelSize(R.dimen.variant_margin)
    }

    var onSelectOptionListener: OnSelectOptionListener? = null

    fun setProductOption(productOption: ProductOption) {

        title.text = productOption.name
        val values = productOption.values
        for (index in values.indices) {
            val optionTextView = TextView(context)
            optionTextView.text = values[index]
            optionTextView.setOnClickListener(this)
            optionTextView.setBackgroundResource(unselectedRes)
            val optionLayoutParams = FlexboxLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            optionLayoutParams.rightMargin = itemMargin
            optionTextView.layoutParams = optionLayoutParams
            options.addView(optionTextView)
            if (index == 0) {
                setDefaultVariant(optionTextView)
            }
        }
    }

    private fun setDefaultVariant(optionTextView: TextView) {
        optionTextView.setBackgroundResource(selectedRes)
        selectedVariantOptions = VariantOption(title.text.toString(), optionTextView.text.toString())
    }

    override fun onClick(view: View) {

        (0 until options.childCount)
                .map { options.getChildAt(it) }
                .forEach {
                    if (it === view && view is TextView) {
                        it.setBackgroundResource(selectedRes)
                        selectedVariantOptions = VariantOption(title.text.toString(), view.text.toString())
                        onSelectOptionListener?.onOptionsSelected()
                    } else {
                        it.setBackgroundResource(unselectedRes)
                    }
                }
    }

    interface OnSelectOptionListener {

        fun onOptionsSelected()
    }
}