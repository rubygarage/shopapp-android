package com.shopify.ui.shipping

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.shopify.api.R
import com.ui.address.AddressFragment

class AddressPageAdapter(
        private val context: Context,
        var sameAddress: Boolean,
        fragmentManager: FragmentManager
) :
        FragmentStatePagerAdapter(fragmentManager) {

    companion object {
        private const val FRAGMENTS_COUNT = 2
        private const val BILLING_ADDRESS_POSITION = 0
        private const val SHIPPING_ADDRESS_POSITION = 1
    }

    override fun getItem(position: Int): Fragment {
        return AddressFragment.newInstance(position == SHIPPING_ADDRESS_POSITION)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            BILLING_ADDRESS_POSITION -> context.getString(R.string.billing_address)
            else -> context.getString(R.string.shipping_address)
        }
    }

    override fun getCount() = FRAGMENTS_COUNT

    fun getBillingFragment(viewPager: ViewPager): AddressFragment? {
        return instantiateItem(viewPager, BILLING_ADDRESS_POSITION) as? AddressFragment
    }

    fun getShippingFragment(viewPager: ViewPager): AddressFragment? {
        return instantiateItem(viewPager, SHIPPING_ADDRESS_POSITION) as? AddressFragment
    }
}