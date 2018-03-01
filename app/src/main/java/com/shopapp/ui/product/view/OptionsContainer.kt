package com.shopapp.ui.product.view

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import com.shopapp.R
import com.shopapp.gateway.entity.ProductOption
import com.shopapp.gateway.entity.VariantOption
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.divider.SpaceDecoration
import com.shopapp.ui.product.adapter.ProductOptionsAdapter
import kotlinx.android.synthetic.main.container_options.view.*

class OptionsContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayoutCompat(context, attrs, defStyleAttr),
    OnItemClickListener {

    private var selectedVariantOptions: VariantOption? = null
    private var adapter: ProductOptionsAdapter? = null
    private var productOption: ProductOption? = null
    var onSelectOptionListener: OnSelectOptionListener? = null

    init {
        orientation = VERTICAL
        View.inflate(context, R.layout.container_options, this)
    }

    fun setProductOption(productOption: ProductOption, preselectedOption: VariantOption?) {
        this.productOption = productOption
        titleText.text = context.getString(R.string.product_option_title_pattern, productOption.name)
        val defaultVariant = preselectedOption
                ?: VariantOption(productOption.name, productOption.values.first())
        adapter = ProductOptionsAdapter(defaultVariant, productOption.values, this)
        val decorator = SpaceDecoration(
            leftSpace = resources.getDimensionPixelSize(R.dimen.variant_margin),
            skipFirst = true
        )
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(decorator)
    }

    fun getSelectedVariantOption() = adapter?.selectedVariantOptions

    override fun onItemClicked(position: Int) {
        productOption?.let {
            val name = it.name
            it.values.getOrNull(position)?.let {
                selectedVariantOptions = VariantOption(name, it)
                adapter?.selectedVariantOptions = selectedVariantOptions
                adapter?.notifyDataSetChanged()
                onSelectOptionListener?.onOptionsSelected()
            }
        }
    }

    interface OnSelectOptionListener {

        fun onOptionsSelected()
    }
}