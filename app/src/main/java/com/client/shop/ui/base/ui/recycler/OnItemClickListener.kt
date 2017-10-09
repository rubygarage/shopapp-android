package com.client.shop.ui.base.ui.recycler

interface OnItemClickListener<T> {
    fun onItemClicked(data: T, position: Int)
}