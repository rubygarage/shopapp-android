package com.shopapp.ui.base.lce

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.R
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView

class TestBaseLceFragment : BaseLceFragment<Any, BaseLceView<Any>, BaseLcePresenter<Any, BaseLceView<Any>>>() {

    override fun inject() {
    }

    override fun getContentView(): Int = R.layout.activity_splash

    override fun createPresenter(): BaseLcePresenter<Any, BaseLceView<Any>> = mock()
}