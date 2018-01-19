package com.client.shop.ui.product.view

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import com.client.shop.R
import com.client.shop.ui.product.adapter.OptionsAdapter
import com.domain.entity.ProductOption
import com.domain.entity.VariantOption
import com.ui.base.recycler.OnItemClickListener
import com.ui.base.recycler.divider.SpaceDecoration
import kotlinx.android.synthetic.main.container_options.view.*

class OptionsContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayoutCompat(context, attrs, defStyleAttr),
    OnItemClickListener {

    private var selectedVariantOptions: VariantOption? = null
    private var adapter: OptionsAdapter? = null
    private var productOption: ProductOption? = null
    var onSelectOptionListener: OnSelectOptionListener? = null

    init {
        orientation = VERTICAL
        View.inflate(context, R.layout.container_options, this)
    }

    fun setProductOption(productOption: ProductOption, preselectedOption: VariantOption?) {
        this.productOption = productOption
        titleText.text = productOption.name
        val defaultVariant = preselectedOption
                ?: VariantOption(productOption.name, productOption.values.first())
        adapter = OptionsAdapter(defaultVariant, productOption.values, this)
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