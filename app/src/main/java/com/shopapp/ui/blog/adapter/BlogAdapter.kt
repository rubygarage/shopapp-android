package com.shopapp.ui.blog.adapter

import android.content.Context
import android.view.View
import com.shopapp.gateway.entity.Article
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.item.ArticleItem

class BlogAdapter(dataList: List<Article>, onItemClickListener: OnItemClickListener) :
    BaseRecyclerAdapter<Article>(dataList, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int): View {
        return ArticleItem(context)
    }

    override fun bindData(itemView: View, data: Article, position: Int) {
        if (itemView is ArticleItem) {
            itemView.setArticle(data)
        }
    }
}