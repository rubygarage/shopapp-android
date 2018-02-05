package com.client.shop.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.client.shop.R
import com.client.shop.ui.account.AccountFragment
import com.client.shop.ui.custom.SimpleOnTabSelectedListener
import com.client.shop.ui.search.SearchWithCategoriesFragment
import com.ui.ext.hideKeyboard
import com.ui.ext.registerKeyboardVisibilityListener
import com.ui.ext.replaceByTag
import kotlinx.android.synthetic.main.activity_home.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar

class HomeActivity : AppCompatActivity() {

    companion object {

        private const val CURRENT_SCREEN = "current_screen"

        private const val HOME = 0
        private const val SEARCH = 1
        private const val ACCOUNT = 2

        fun getStartIntent(context: Context, isNewTask: Boolean = false): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            if (isNewTask) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            return intent
        }
    }

    private var unregistrar: Unregistrar? = null
    private var currentScreen: Int = HOME

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        currentScreen = savedInstanceState?.getInt(CURRENT_SCREEN, HOME) ?: HOME
        setupNavigation()
        switchFragment(currentScreen)
    }

    override fun onResume() {
        super.onResume()
        unregistrar = registerKeyboardVisibilityListener(KeyboardVisibilityEventListener {
            bottomTabNavigation.visibility = if (it) View.GONE else View.VISIBLE
        })
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
        unregistrar?.unregister()
    }

    override fun onBackPressed() {
        if (bottomTabNavigation.selectedTabPosition == SEARCH) {
            val searchFragment: Fragment? = supportFragmentManager.findFragmentByTag(SEARCH.toString())
            if (searchFragment is SearchWithCategoriesFragment && searchFragment.onBackPressed()) {
                selectTab(HOME)
            }
        } else if (bottomTabNavigation.selectedTabPosition != HOME) {
            selectTab(HOME)
        } else {
            finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(CURRENT_SCREEN, currentScreen)
    }

    //SETUP

    private fun setupNavigation() {
        bottomTabNavigation.getTabAt(currentScreen)?.select()
        bottomTabNavigation.addOnTabSelectedListener(object : SimpleOnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                switchFragment(tab.position)
            }
        })
    }

    private fun switchFragment(position: Int) {
        currentScreen = position
        supportFragmentManager.replaceByTag(R.id.content, position.toString(), {
            when (position) {
                HOME -> HomeFragment()
                SEARCH -> SearchWithCategoriesFragment()
                else -> AccountFragment()
            }
        }).commit()
    }

    private fun selectTab(position: Int) {
        bottomTabNavigation.getTabAt(position)?.select()
    }
}