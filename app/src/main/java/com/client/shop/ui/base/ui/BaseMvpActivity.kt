package com.client.shop.ui.base.ui

import android.os.Bundle
import com.client.shop.MainApplication
import com.client.shop.di.component.AppComponent
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.hannesdorfmann.mosby3.mvp.viewstate.MvpViewStateActivity
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState

abstract class BaseMvpActivity<V : MvpView, P : MvpPresenter<V>, VS : ViewState<V>> :
        MvpViewStateActivity<V, P, VS>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(MainApplication.appComponent)
        super.onCreate(savedInstanceState)
    }

    abstract fun inject(component: AppComponent)
}