package com.client.shop.ui.base.contract

import android.support.annotation.StringRes
import com.hannesdorfmann.mosby3.mvp.MvpView

interface BaseMvpView<in T> : MvpView {

    fun showContent(data: T)

    fun showError(isNetworkError: Boolean)

    fun showMessage(@StringRes messageRes: Int)

    fun showActionError(action: String) {

    }
}