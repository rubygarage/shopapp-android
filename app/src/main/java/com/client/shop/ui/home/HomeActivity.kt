package com.client.shop.ui.home

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.auth.AuthActivity
import com.client.shop.ui.base.ui.lce.BaseActivity
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.category.CategoryFragment
import com.client.shop.ui.home.adapter.CategoriesAdapter
import com.client.shop.ui.home.contract.HomePresenter
import com.client.shop.ui.home.contract.HomeView
import com.client.shop.ui.home.di.HomeModule
import com.client.shop.ui.policy.PolicyActivity
import com.domain.entity.Category
import com.domain.entity.Policy
import com.domain.entity.Shop
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity :
        BaseActivity<Pair<Shop, List<Category>>, HomeView, HomePresenter>(),
        HomeView,
        OnItemClickListener<Category> {

    @Inject lateinit var homePresenter: HomePresenter
    private var drawerToggle: ActionBarDrawerToggle? = null
    private var categories: MutableList<Category> = mutableListOf()
    private var adapter: CategoriesAdapter? = null

    companion object {

        fun getStartIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDrawerLayout()
        setupRecyclerView(categories)

        homeLabel.setOnClickListener {
            drawerLayout.closeDrawer(Gravity.START)
            supportFragmentManager.beginTransaction().replace(R.id.content, HomeFragment()).commit()
        }
        accountLabel.setOnClickListener { startActivity(AuthActivity.getStartIntent(this)) }

        loadData(false)
    }

    //INIT

    override fun inject(component: AppComponent) {
        component.attachHomeComponent(HomeModule()).inject(this)
    }

    override fun getMainLayout() = R.layout.activity_home

    override fun getContentView() = R.layout.activity_home_content

    override fun createPresenter() = homePresenter

    //ACTIVITY

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

    //SETUP

    private fun setupDrawerLayout() {
        drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.open_menu,
                R.string.close_menu
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }
    }

    private fun setupRecyclerView(categories: List<Category>) {
        adapter = CategoriesAdapter(categories, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupShopInfo(policy: Policy?, view: View) {
        if (policy != null) {
            view.visibility = View.VISIBLE
            view.setOnClickListener { startActivity(PolicyActivity.getStartIntent(this, policy)) }
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.requestData()
    }

    override fun showContent(data: Pair<Shop, List<Category>>) {
        super.showContent(data)

        val shop = data.first
        categories.clear()
        categories.addAll(data.second)
        adapter?.notifyDataSetChanged()

        setupShopInfo(shop.privacyPolicy, privacyPolicyLabel)
        setupShopInfo(shop.termsOfService, termsOfServiceLabel)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
        supportFragmentManager.beginTransaction().replace(R.id.content, HomeFragment()).commitNowAllowingStateLoss()
    }

    //CALLBACK

    override fun onItemClicked(data: Category, position: Int) {
        drawerLayout.closeDrawer(Gravity.START)
        supportFragmentManager.beginTransaction().replace(R.id.content, CategoryFragment.newInstance(data)).commit()
    }
}