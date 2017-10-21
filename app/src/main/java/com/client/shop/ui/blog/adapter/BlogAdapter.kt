package com.client.shop.ui.blog.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.client.shop.ui.base.ui.recycler.BaseRecyclerAdapter
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.item.ArticleItem
import com.client.shop.ui.item.MoreItem
import com.domain.entity.Article

class BlogAdapter(dataList: List<Article>, onItemClickListener: OnItemClickListener<Article>,
                  private val moreButtonListener: View.OnClickListener? = null) :
        BaseRecyclerAdapter<Article>(dataList, onItemClickListener) {

    override fun getItemView(context: Context, viewType: Int) = ArticleItem(context)

    override fun bindData(itemView: View, data: Article, position: Int) {
        if (itemView is ArticleItem) {
            itemView.setArticle(data)
        }
    }

    override fun getFooterView(context: Context): View? {
        val footer = MoreItem(context, height = ViewGroup.LayoutParams.WRAP_CONTENT)
        if (moreButtonListener != null) {
            footer.moreButton.setOnClickListener(moreButtonListener)
        }
        return footer
    }
}