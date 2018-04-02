package com.shopapp.ui.base.lce

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import com.shopapp.R
import com.shopapp.gateway.entity.Error
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import com.shopapp.ui.base.lce.view.LceEmptyView
import com.shopapp.ui.base.lce.view.LceLayout
import kotlinx.android.synthetic.main.fragment_lce.*
import kotlinx.android.synthetic.main.layout_lce.*

abstract class BaseLceFragment<in M, V : BaseLceView<M>, P : BaseLcePresenter<M, V>> :
    MvpFragment<V, P>(),
    BaseLceView<M> {

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getRootView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lceLayout.setupContentLayout(getContentView())
        lceLayout.tryAgainButtonClickListener = View.OnClickListener { tryAgainButtonClicked() }
        lceLayout.backButtonClickListener = View.OnClickListener { activity?.onBackPressed() }
        setupEmptyView(emptyView)
    }

    //INIT

    abstract fun inject()

    @LayoutRes
    protected open fun getRootView() = R.layout.fragment_lce

    @LayoutRes
    abstract fun getContentView(): Int

    //SETUP

    protected open fun setupEmptyView(emptyView: LceEmptyView) {
    }

    //LCE

    @CallSuper
    open fun loadData(pullToRefresh: Boolean = false) {
        if (!pullToRefresh) {
            showLoading()
        }
    }

    @CallSuper
    override fun showContent(data: M) {
        lceLayout.changeState(LceLayout.LceState.ContentState)
    }

    override fun showLoading(isTranslucent: Boolean) {
        lceLayout.changeState(LceLayout.LceState.LoadingState(isTranslucent))
    }

    override fun showEmptyState() {
        lceLayout.changeState(LceLayout.LceState.EmptyState)
    }

    override fun showError(error: Error) {
        lceLayout.changeState(LceLayout.LceState.ErrorState(error))
    }

    override fun showMessage(messageRes: Int) {
        showMessage(getString(messageRes))
    }

    override fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    //CALLBACK

    open fun tryAgainButtonClicked() {
        loadData()
    }
}