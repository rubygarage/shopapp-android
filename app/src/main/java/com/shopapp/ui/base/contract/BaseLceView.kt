package com.shopapp.ui.base.contract

import android.support.annotation.StringRes
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.shopapp.gateway.entity.Error

interface BaseLceView<in T> : MvpView {

    fun showContent(data: T)

    fun showLoading(isTranslucent: Boolean = false)

    fun showEmptyState()

    fun showError(error: Error)

    fun showMessage(@StringRes messageRes: Int)

    fun showMessage(message: String)

    fun showActionError(action: String) {

    }
}