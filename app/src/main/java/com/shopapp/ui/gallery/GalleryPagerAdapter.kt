package com.shopapp.ui.gallery

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.shopapp.gateway.entity.Product

class GalleryPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    var product: Product? = null
    var isThumbnailMode: Boolean = false
    var imageClickListener: View.OnClickListener? = null

    override fun getItem(position: Int): Fragment {
        val fragment = ImageFragment.newInstance(product?.images?.get(position), isThumbnailMode)
        fragment.imageClickListener = imageClickListener
        return fragment
    }

    override fun getCount() = product?.images?.size ?: 0
}