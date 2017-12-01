package com.ui.base.lce

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import com.ui.R
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import com.ui.base.lce.view.LceLayout
import kotlinx.android.synthetic.main.fragment_lce.*

abstract class BaseFragment<in M, V : BaseLceView<M>, P : BaseLcePresenter<M, V>> :
        MvpFragment<V, P>(),
        BaseLceView<M> {

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_lce, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lceLayout.setupContentLayout(getContentView())
        lceLayout.tryAgainButtonClickListener = View.OnClickListener { tryAgainButtonClicked() }
    }

    //INIT

    abstract fun inject()

    @LayoutRes
    abstract fun getContentView(): Int

    //LCE

    @CallSuper
    open fun loadData(pullToRefresh: Boolean  = false) {
        if (!pullToRefresh) {
            lceLayout.changeState(LceLayout.LceState.ContentState)
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
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    //CALLBACK

    open fun tryAgainButtonClicked() {
        loadData()
    }
}