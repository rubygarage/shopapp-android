package com.client.shop.ui.base.contract

import com.hannesdorfmann.mosby3.mvp.MvpView

interface BaseMvpViewLce<in T> : MvpView {

    fun showContent(data: T)

    fun showError(isNetworkError: Boolean)

    fun showMessage(message: String)

    fun showActionError(action: String) {

    }
}