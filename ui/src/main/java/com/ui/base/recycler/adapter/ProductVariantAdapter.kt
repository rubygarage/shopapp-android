package com.ui.base.recycler.adapter

import android.content.Context
import android.view.View
import com.domain.entity.ProductVariant
import com.ui.base.item.ProductVariantItem
import com.ui.base.recycler.OnItemClickListener

class ProductVariantAdapter(
    dataList: List<ProductVariant>,
    onItemClickListener: OnItemClickListener
) :
    BaseRecyclerAdapter<ProductVariant>(dataList, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int) = ProductVariantItem(context)

    override fun bindData(itemView: View, data: ProductVariant, position: Int) {
        if (itemView is ProductVariantItem) {
            val imageURI: String? = data.image?.src ?: data.productImage?.src
            itemView.setImageURI(imageURI)
        }
    }
}