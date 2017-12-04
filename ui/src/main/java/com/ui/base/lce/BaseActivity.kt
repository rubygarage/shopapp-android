package com.ui.base.lce

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.ui.R
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import com.ui.base.lce.view.LceLayout
import kotlinx.android.synthetic.main.activity_lce.*

abstract class BaseActivity<in M, V : BaseLceView<M>, P : BaseLcePresenter<M, V>> :
        MvpActivity<V, P>(),
        BaseLceView<M> {

    //ACTIVITY

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(getMainLayout())
        lceLayout.setupContentLayout(getContentView())
        lceLayout.tryAgainButtonClickListener = View.OnClickListener { tryAgainButtonClicked() }
        lceLayout.emptyButtonClickListener = View.OnClickListener { emptyButtonClicked() }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
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

    open fun changeState(state: LceLayout.LceState) {
        lceLayout.changeState(state)
    }

    @CallSuper
    open fun loadData(pullToRefresh: Boolean = false) {
        if (!pullToRefresh) {
            changeState(LceLayout.LceState.LoadingState)
        }
    }

    @CallSuper
    override fun showContent(data: M) {
        changeState(LceLayout.LceState.ContentState)
    }

    override fun showEmptyState() {
        changeState(LceLayout.LceState.EmptyState)
    }

    @CallSuper
    override fun showError(isNetworkError: Boolean) {
        changeState(LceLayout.LceState.ErrorState(isNetworkError))
    }

    override fun showMessage(messageRes: Int) {
        showMessage(getString(messageRes))
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    //CALLBACK

    open fun tryAgainButtonClicked() {
        loadData()
    }

    open fun emptyButtonClicked() {

    }
}