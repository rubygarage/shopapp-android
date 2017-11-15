package com.client.shop.ui.base.ui.recycler

interface OnItemClickListener {

    fun onHeaderClicked() {

    }

    fun onItemClicked(position: Int)

    fun onFooterClicked() {

    }
}