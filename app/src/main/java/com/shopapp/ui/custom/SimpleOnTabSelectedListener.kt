package com.shopapp.ui.custom

import android.support.design.widget.TabLayout

interface SimpleOnTabSelectedListener : TabLayout.OnTabSelectedListener {

    override fun onTabReselected(tab: TabLayout.Tab) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
    }
}