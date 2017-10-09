package com.client.shop.ui.base.contract

import com.hannesdorfmann.mosby3.mvp.MvpView

interface BaseMvpView : MvpView {

    fun showMessage(message: String, isError: Boolean) {

    }

    fun showProgress() {

    }

    fun hideProgress() {

    }
}