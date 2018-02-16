package com.client.shop.ui.base.lce

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.client.shop.R
import com.client.shop.ext.hideKeyboard
import com.client.shop.ui.base.contract.BaseLcePresenter
import com.client.shop.ui.base.contract.BaseLceView
import com.client.shop.ui.base.lce.view.LceEmptyView
import com.client.shop.ui.base.lce.view.LceLayout
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.*

abstract class BaseLceActivity<in M, V : BaseLceView<M>, P : BaseLcePresenter<M, V>> :
    MvpActivity<V, P>(),
    BaseLceView<M> {

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(getMainLayout())
        lceLayout.setupContentLayout(getContentView())
        lceLayout.tryAgainButtonClickListener = View.OnClickListener { tryAgainButtonClicked() }
        lceLayout.emptyButtonClickListener = View.OnClickListener { emptyButtonClicked() }
        toolbar?.let {
            setSupportActionBar(it)
        }
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(!useModalStyle())
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        }
        if (useModalStyle()) {
            overridePendingTransition(R.anim.slide_in, R.anim.no_animation)
        }
        setupEmptyView(emptyView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (useModalStyle()) {
            menuInflater.inflate(R.menu.menu_modal, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item?.itemId
        if (itemId == android.R.id.home || itemId == R.id.close) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    fun setTitle(title: String) {
        toolbar?.setTitle(title)
    }

    override fun finish() {
        super.finish()
        if (useModalStyle()) {
            overridePendingTransition(R.anim.no_animation, R.anim.slide_out)
        }
    }

    protected open fun useModalStyle() = false

    @Deprecated("Use an overloaded function instead", ReplaceWith("setTitle(title: String)"))
    override fun setTitle(titleId: Int) {
        super.setTitle(titleId)
    }

    @Deprecated("Use an overloaded function instead", ReplaceWith("setTitle(title: String)"))
    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
    }

    //INIT

    abstract fun inject()

    @LayoutRes
    protected open fun getMainLayout(): Int {
        return R.layout.activity_lce
    }

    //SETUP

    protected open fun setupEmptyView(emptyView: LceEmptyView) {
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
            showLoading()
        }
    }

    override fun showLoading(isTranslucent: Boolean) {
        changeState(LceLayout.LceState.LoadingState(isTranslucent))
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