package com.client.shop.ui.base.ui.lce

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Toast
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.contract.BaseView
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.custom.lce.LceLayout
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_lce.*

abstract class BaseActivity<in M, V : BaseView<M>, P : BasePresenter<M, V>> :
        MvpActivity<V, P>(),
        BaseView<M> {

    //ACTIVITY

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(ShopApplication.appComponent)
        super.onCreate(savedInstanceState)
        setContentView(getMainLayout())
        lceLayout.setupContentLayout(getContentView())
        lceLayout.tryAgainButtonClickListener = View.OnClickListener { tryAgainButtonClicked() }
        lceLayout.emptyButtonClickListener = View.OnClickListener { emptyButtonClicked() }
        setSupportActionBar(toolbar)
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

    override fun showEmptyState() {
        lceLayout.changeState(LceLayout.LceState.EmptyState)
    }

    @CallSuper
    override fun showError(isNetworkError: Boolean) {
        lceLayout.changeState(LceLayout.LceState.ErrorState(isNetworkError))
    }

    override fun showMessage(messageRes: Int) {
        showMessage(getString(messageRes))
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //CALLBACK

    open fun tryAgainButtonClicked() {
        loadData(false)
    }

    open fun emptyButtonClicked() {

    }
}