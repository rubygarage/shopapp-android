package com.client.shop.ui.base.ui.recycler

interface OnItemClickListener<in T> {
    fun onItemClicked(data: T, position: Int)
}