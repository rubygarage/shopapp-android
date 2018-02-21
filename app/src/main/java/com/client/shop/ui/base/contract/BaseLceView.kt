package com.client.shop.ui.base.contract

import android.support.annotation.StringRes
import com.hannesdorfmann.mosby3.mvp.MvpView

interface BaseLceView<in T> : MvpView {

    fun showContent(data: T)

    fun showLoading(isTranslucent: Boolean = false)

    fun showEmptyState()

    fun showError(isNetworkError: Boolean)

    fun showMessage(@StringRes messageRes: Int)

    fun showMessage(message: String)

    fun showActionError(action: String) {

    }
}