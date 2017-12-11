package com.client.shop.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import com.client.shop.R
import com.client.shop.ext.replaceByTag
import com.client.shop.ui.account.AccountFragment
import com.client.shop.ui.custom.SimpleOnTabSelectedListener
import com.client.shop.ui.search.SearchFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    companion object {

        private const val HOME = 0
        private const val SEARCH = 1
        private const val ACCOUNT = 2

        fun getStartIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupNavigation()
        switchFragment(HOME)
    }

    override fun onBackPressed() {
        if (bottomTabNavigation.selectedTabPosition != HOME) {
            selectTab(HOME)
        } else {
            finish()
        }
    }

    //SETUP

    private fun setupNavigation() {
        bottomTabNavigation.addOnTabSelectedListener(object : SimpleOnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                switchFragment(tab.position)
            }
        })
    }

    private fun switchFragment(position: Int) {
        supportFragmentManager.replaceByTag(R.id.content, position.toString(), {
            when (position) {
                HOME -> HomeFragment()
                SEARCH -> SearchFragment()
                else -> AccountFragment()
            }
        }).commit()
    }

    private fun selectTab(position: Int) {
        bottomTabNavigation.getTabAt(position)?.select()
    }
}