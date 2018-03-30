package com.shopapp.ui.product.adapter

import android.content.Context
import android.view.View
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.item.ProductVariantItem

class ProductVariantAdapter(
    dataList: List<ProductVariant?>,
    onItemClickListener: OnItemClickListener
) :
    BaseRecyclerAdapter<ProductVariant?>(dataList, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int) = ProductVariantItem(context)

    override fun bindData(itemView: View, data: ProductVariant?, position: Int) {
        if (itemView is ProductVariantItem) {
            val imageURI: String? = data?.image?.src ?: data?.productImage?.src
            itemView.setImageURI(imageURI)
        }
    }
}