package com.client.shop.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.auth.contract.AuthPresenter
import com.client.shop.ui.auth.contract.AuthView
import com.client.shop.ui.auth.di.AuthModule
import com.ui.lce.BaseActivity
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : BaseActivity<Boolean, AuthView, AuthPresenter>(), AuthView {

    @Inject lateinit var authPresenter: AuthPresenter

    companion object {
        fun getStartIntent(context: Context) = Intent(context, AuthActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(R.string.account)
        loadData()
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent(AuthModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_auth

    override fun createPresenter() = authPresenter

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.isAuthorized()
    }

    override fun showContent(data: Boolean) {
        super.showContent(data)
        if (data) {
            //TODO ADD LOGOUT
        } else {
            setupViewPager()
        }
    }

    //SETUP

    private fun setupViewPager() {
        tabLayout.visibility = View.VISIBLE
        viewPager.visibility = View.VISIBLE
        viewPager.adapter = AuthPagerAdapter(this, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}