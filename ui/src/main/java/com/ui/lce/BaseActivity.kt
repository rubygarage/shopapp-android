package com.ui.lce

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.ui.R
import com.ui.contract.BasePresenter
import com.ui.contract.BaseView
import com.ui.lce.view.LceLayout
import kotlinx.android.synthetic.main.activity_lce.*

abstract class BaseActivity<in M, V : BaseView<M>, P : BasePresenter<M, V>> :
        MvpActivity<V, P>(),
        BaseView<M> {

    //ACTIVITY

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(getMainLayout())
        lceLayout.setupContentLayout(getContentView())
        lceLayout.tryAgainButtonClickListener = View.OnClickListener { tryAgainButtonClicked() }
        lceLayout.emptyButtonClickListener = View.OnClickListener { emptyButtonClicked() }
        setSupportActionBar(toolbar)
    }

    //INIT

    abstract fun inject()

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