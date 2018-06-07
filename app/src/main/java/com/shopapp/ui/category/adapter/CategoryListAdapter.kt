package com.shopapp.ui.category.adapter

import android.content.Context
import android.view.View
import com.shopapp.gateway.entity.Category
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.item.category.CategoryGridItem

class CategoryListAdapter(
    categories: List<Category>,
    onItemClickListener: OnItemClickListener
) :
    BaseRecyclerAdapter<Category>(categories, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int) = CategoryGridItem(context)

    override fun bindData(itemView: View, data: Category, position: Int) {
        if (itemView is CategoryGridItem) {
            itemView.setCategory(data)
        }
    }
}