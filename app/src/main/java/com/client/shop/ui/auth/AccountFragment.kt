package com.client.shop.ui.auth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.auth.contract.AuthPresenter
import com.client.shop.ui.auth.contract.AuthView
import com.client.shop.ui.auth.di.AuthModule
import com.ui.base.lce.BaseFragment
import kotlinx.android.synthetic.main.fragment_account.*
import javax.inject.Inject

class AccountFragment : BaseFragment<Boolean, AuthView, AuthPresenter>(), AuthView {

    @Inject lateinit var authPresenter: AuthPresenter

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val compatActivity = activity
        if (compatActivity is AppCompatActivity) {
            toolbar.setTitle(getString(R.string.account))
            compatActivity.setSupportActionBar(toolbar)
            compatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachAuthComponent(AuthModule()).inject(this)
    }

    override fun getContentView() = R.layout.fragment_account

    override fun createPresenter() = authPresenter

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.isAuthorized()
    }

    override fun showContent(data: Boolean) {
        super.showContent(data)
        if (data) {
            setupLogout()
        } else {
            setupViewPager()
        }
    }

    override fun signedOut() {
        showMessage(R.string.logout_success_message)
        loadData()
    }

    //SETUP

    private fun setupViewPager() {
        logoutButton.visibility = View.GONE
        tabLayout.visibility = View.VISIBLE
        viewPager.visibility = View.VISIBLE
        viewPager.adapter = AuthPagerAdapter(context, childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupLogout() {
        logoutButton.visibility = View.VISIBLE
        tabLayout.visibility = View.GONE
        viewPager.visibility = View.GONE
        logoutButton.setOnClickListener { presenter.signOut() }
    }
}