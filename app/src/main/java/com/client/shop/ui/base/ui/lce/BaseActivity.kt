package com.client.shop.ui.base.ui.lce

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.widget.Toast
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.custom.lce.LceLayout
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.view_lce_error.*

abstract class BaseActivity<in M, V : BaseMvpView<M>, P : BasePresenter<M, V>> :
        MvpActivity<V, P>(),
        BaseMvpView<M> {

    //ACTIVITY

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(ShopApplication.appComponent)
        super.onCreate(savedInstanceState)
        setContentView(getMainLayout())
        lceLayout.setupContentLayout(getContentView())
        setSupportActionBar(toolbar)
        tryAgainButton.setOnClickListener { tryAgainButtonClicked() }
    }

    //INIT

    abstract fun inject(component: AppComponent)

    @LayoutRes
    open protected fun getMainLayout(): Int {
        return R.layout.activity_lce
    }

    @LayoutRes
    abstract fun getContentView(): Int

    //LCE

    @CallSuper
    open fun loadData(pullToRefresh: Boolean) {
        if (!pullToRefresh) {
            lceLayout.changeState(LceLayout.LceState.LoadingState)
        }
    }

    @CallSuper
    override fun showContent(data: M) {
        lceLayout.changeState(LceLayout.LceState.ContentState)
    }

    @CallSuper
    override fun showError(isNetworkError: Boolean) {
        lceLayout.changeState(LceLayout.LceState.ErrorState(isNetworkError))
    }

    override fun showMessage(messageRes: Int) {
        Toast.makeText(this, getString(messageRes), Toast.LENGTH_LONG).show()
    }

    open fun tryAgainButtonClicked() {
        loadData(false)
    }
}