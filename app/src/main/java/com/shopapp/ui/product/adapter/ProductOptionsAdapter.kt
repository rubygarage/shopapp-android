package com.shopapp.ui.product.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.shopapp.R
import com.shopapp.gateway.entity.VariantOption
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter

class ProductOptionsAdapter(
    defaultVariantOption: VariantOption,
    dataList: List<String>,
    onItemClickListener: OnItemClickListener
) :
    BaseRecyclerAdapter<String>(dataList, onItemClickListener) {

    var selectedVariantOptions: VariantOption? = defaultVariantOption
    private val selectedBackgroundRes: Int = R.drawable.background_selected_variant
    private val unselectedBackgroundRes: Int = R.drawable.background_unselected_variant
    private val selectedTextRes: Int = R.color.white
    private val unselectedTextRes: Int = R.color.black

    override fun getItemView(context: Context, viewType: Int): View {
        val itemView = TextView(context)
        itemView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
            context.resources.getDimension(R.dimen.variant_text_size))
        return itemView
    }

    override fun bindData(itemView: View, data: String, position: Int) {
        if (itemView is TextView) {
            itemView.text = data
            val background = if (selectedVariantOptions?.value == data) selectedBackgroundRes else unselectedBackgroundRes
            val textColorRes = if (selectedVariantOptions?.value == data) selectedTextRes else unselectedTextRes
            itemView.setBackgroundResource(background)
            itemView.setTextColor(ContextCompat.getColor(itemView.context, textColorRes))
        }
    }
}