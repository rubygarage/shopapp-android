package com.client.shop.ui.home

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import com.client.shop.R
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.category.CategoryFragment
import com.client.shop.ui.home.adapter.CategoriesAdapter
import com.client.shop.ui.policy.PolicyActivity
import com.domain.entity.Category
import com.domain.entity.Policy
import com.domain.entity.Shop
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), OnItemClickListener<Category> {

    private var drawerToggle: ActionBarDrawerToggle? = null

    companion object {

        private const val EXTRA_SHOP = "EXTRA_SHOP"
        private const val EXTRA_CATEGORIES = "EXTRA_CATEGORIES"

        fun getStartIntent(context: Context, shop: Shop, categories: List<Category>): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(EXTRA_SHOP, shop)
            intent.putParcelableArrayListExtra(EXTRA_CATEGORIES, ArrayList(categories))
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val shop: Shop = intent.getParcelableExtra(EXTRA_SHOP)
        val categories: List<Category> = intent.getParcelableArrayListExtra(EXTRA_CATEGORIES)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }
        setupDrawerLayout()
        setupRecyclerView(categories)
        initShopInfo(shop.privacyPolicy, privacyPolicyLabel)
        initShopInfo(shop.termsOfService, termsOfServiceLabel)

        homeLabel.setOnClickListener {
            drawerLayout.closeDrawer(Gravity.START)
            supportFragmentManager.beginTransaction().replace(R.id.content, HomeFragment()).commit()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.content, HomeFragment()).commit()
        }
    }

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

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CategoriesAdapter(categories, this)
    }

    private fun initShopInfo(policy: Policy?, view: View) {
        if (policy != null) {
            view.visibility = View.VISIBLE
            view.setOnClickListener { startActivity(PolicyActivity.getStartIntent(this, policy)) }
        }
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

    override fun onItemClicked(data: Category, position: Int) {
        drawerLayout.closeDrawer(Gravity.START)
        supportFragmentManager.beginTransaction().replace(R.id.content, CategoryFragment.newInstance(data)).commit()
    }
}