package com.client.shop.ui.home

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.auth.AccountFragment
import com.client.shop.ui.custom.SimpleOnTabSelectedListener
import com.client.shop.ui.home.contract.HomePresenter
import com.client.shop.ui.home.contract.HomeView
import com.client.shop.ui.home.di.HomeModule
import com.client.shop.ui.search.SearchFragment
import com.domain.entity.Category
import com.domain.entity.Shop
import com.ui.base.lce.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity :
        BaseActivity<Pair<Shop, List<Category>>, HomeView, HomePresenter>(),
        HomeView {

    @Inject lateinit var homePresenter: HomePresenter
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var categories: MutableList<Category> = mutableListOf()

    companion object {

        private const val HOME = 0
        private const val SEARCH = 1
        private const val ACCOUNT = 2

        fun getStartIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (drawerToggle?.onOptionsItemSelected(item) == true) {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (bottomTabNavigation.selectedTabPosition != HOME) {
            selectTab(HOME)
        } else {
            finish()
        }
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachHomeComponent(HomeModule()).inject(this)
    }

    override fun getMainLayout() = R.layout.activity_home

    override fun getContentView() = R.layout.activity_home_content

    override fun createPresenter() = homePresenter

    //SETUP

    private fun setupNavigation() {
        bottomTabNavigation.addOnTabSelectedListener(object : SimpleOnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                switchFragment(tab.position)
            }
        })
    }

    private fun switchFragment(position: Int) {
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(position.toString())
        if (fragment == null) {
            fragment = when (position) {
                HOME -> HomeFragment()
                SEARCH -> SearchFragment()
                else -> AccountFragment()
            }
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment, position.toString())
                .addToBackStack(null)
                .commit()
    }

    private fun selectTab(position: Int) {
        bottomTabNavigation.getTabAt(position)?.select()
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.requestData()
    }

    override fun showContent(data: Pair<Shop, List<Category>>) {
        super.showContent(data)

        bottomTabNavigation.visibility = View.VISIBLE
        val shop = data.first
        categories.clear()
        categories.addAll(data.second)
        setupNavigation()
        switchFragment(HOME)
    }
}