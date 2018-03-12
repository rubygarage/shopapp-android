package com.shopapp.ui.base.recycler.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.shopapp.ui.base.recycler.OnItemClickListener

abstract class BaseRecyclerAdapter<T>(protected val dataList: List<T>,
                                      protected val onItemClickListener: OnItemClickListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var withHeader = false
    var withFooter = false

    companion object {
        private const val DEFAULT_TYPE = 100
        private const val HEADER_TYPE = 200
        private const val FOOTER_TYPE = 300
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return when (viewType) {
            HEADER_TYPE -> HeaderViewHolder(getHeaderView(context))
            FOOTER_TYPE -> FooterViewHolder(getFooterView(context))
            else -> getItemHolder(context, viewType)
        }
    }

    protected open fun getItemHolder(context: Context, viewType: Int): RecyclerView.ViewHolder {
        return DefaultViewHolder(getItemView(context, viewType), onItemClickListener, withHeader)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemPosition = position - getHeaderOffset()
        if (itemPosition >= 0 && dataList.size > itemPosition) {
            val itemView = holder.itemView
            val data = dataList[itemPosition]
            bindData(itemView, data, itemPosition)
        } else if (dataList.size == itemPosition && withFooter) {
            bindFooterData(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int = dataList.size + getHeaderOffset() + getFooterOffset()

    override fun getItemViewType(position: Int): Int {
        return if (withHeader && position == 0) {
            HEADER_TYPE
        } else if (withFooter && position == itemCount - 1) {
            FOOTER_TYPE
        } else {
            DEFAULT_TYPE
        }
    }

    private fun getHeaderOffset() = if (withHeader) 1 else 0

    private fun getFooterOffset() = if (withFooter) 1 else 0

    abstract fun getItemView(context: Context, viewType: Int): View

    abstract fun bindData(itemView: View, data: T, position: Int)

    protected open fun bindFooterData(itemView: View, position: Int) {

    }

    open fun getHeaderView(context: Context): View? = null

    open fun getFooterView(context: Context): View? = null

    protected open fun setOnClickListenerForEachItem() {

    }

    class DefaultViewHolder(itemView: View, onItemClickListener: OnItemClickListener?, withHeader: Boolean) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                val headerOffset = if (withHeader) 1 else 0
                val position = adapterPosition - headerOffset
                if (position >= 0) {
                    onItemClickListener?.onItemClicked(position)
                }
            }
        }
    }

    class HeaderViewHolder(headerView: View?) : RecyclerView.ViewHolder(headerView)

    class FooterViewHolder(footerView: View?) : RecyclerView.ViewHolder(footerView)
}