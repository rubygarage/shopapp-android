package com.ui.contract

import android.support.annotation.StringRes
import com.hannesdorfmann.mosby3.mvp.MvpView

interface BaseView<in T> : MvpView {

    fun showContent(data: T)

    fun showEmptyState()

    fun showError(isNetworkError: Boolean)

    fun showMessage(@StringRes messageRes: Int)

    fun showMessage(message: String)

    fun showActionError(action: String) {

    }
}