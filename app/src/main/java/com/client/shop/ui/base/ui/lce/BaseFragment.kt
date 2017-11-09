package com.client.shop.ui.base.ui.lce

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.custom.lce.LceLayout
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import kotlinx.android.synthetic.main.activity_lce.*

abstract class BaseFragment<in M, V : BaseMvpView<M>, P : BasePresenter<M, V>> :
        MvpFragment<V, P>(),
        BaseMvpView<M> {

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        inject(ShopApplication.appComponent)
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

    abstract fun inject(component: AppComponent)

    @LayoutRes
    abstract fun getContentView(): Int

    //LCE

    @CallSuper
    open fun loadData(pullToRefresh: Boolean) {
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
        loadData(false)
    }
}