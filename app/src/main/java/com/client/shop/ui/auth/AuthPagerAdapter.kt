package com.client.shop.ui.auth

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.client.shop.R

class AuthPagerAdapter(private val context: Context, fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {

    companion object {

        private const val FRAGMENTS_COUNT = 2
        private const val SIGN_UP = 0
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            SIGN_UP -> SignUpFragment()
            else -> SignInFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            SIGN_UP -> context.getString(R.string.register)
            else -> context.getString(R.string.login)
        }
    }

    override fun getCount() = FRAGMENTS_COUNT
}