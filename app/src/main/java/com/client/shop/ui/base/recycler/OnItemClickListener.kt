package com.client.shop.ui.base.recycler

interface OnItemClickListener {

    fun onHeaderClicked() {

    }

    fun onItemClicked(position: Int)

    fun onFooterClicked() {

    }
}