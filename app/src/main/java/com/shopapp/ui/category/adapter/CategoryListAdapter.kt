package com.shopapp.ui.category.adapter

import android.content.Context
import android.view.View
import com.shopapp.gateway.entity.Category
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.item.category.BaseCategoryItem
import com.shopapp.ui.item.category.CategoryGridItem
import com.shopapp.ui.item.category.CategoryItem

class CategoryListAdapter(
    categories: List<Category>,
    onItemClickListener: OnItemClickListener,
    private val isGridMode: Boolean = true
) :
    BaseRecyclerAdapter<Category>(categories, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int) =
        if (isGridMode) CategoryGridItem(context) else CategoryItem(context)

    override fun bindData(itemView: View, data: Category, position: Int) {
        if (itemView is BaseCategoryItem) {
            itemView.setCategory(data)
        }
    }
}