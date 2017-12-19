package com.client.shop.ui.blog.adapter

import android.content.Context
import android.view.View
import com.ui.base.recycler.OnItemClickListener
import com.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.client.shop.ui.item.ArticleItem
import com.domain.entity.Article

class BlogAdapter(dataList: List<Article>, onItemClickListener: OnItemClickListener) :
        BaseRecyclerAdapter<Article>(dataList, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int) = ArticleItem(context)

    override fun bindData(itemView: View, data: Article, position: Int) {
        if (itemView is ArticleItem) {
            itemView.setArticle(data)
        }
    }
}