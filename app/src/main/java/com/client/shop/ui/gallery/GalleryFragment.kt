package com.client.shop.ui.gallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.domain.entity.Product
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : Fragment() {

    private var product: Product? = null
    private var isThumbnailMode: Boolean = false
    private lateinit var adapter: GalleryPagerAdapter
    var imageClickListener: View.OnClickListener? = null

    companion object {

        private const val PRODUCT = "product"
        private const val IS_THUMBNAIL_MODE = "is_thumbnail_mode"
        private const val SELECTED_POSITION = "selected_position"

        fun newInstance(product: Product? = null, isThumbnailMode: Boolean = false, selectedPosition: Int = 0): GalleryFragment {
            val fragment = GalleryFragment()
            val args = Bundle()
            args.putParcelable(PRODUCT, product)
            args.putBoolean(IS_THUMBNAIL_MODE, isThumbnailMode)
            args.putInt(SELECTED_POSITION, selectedPosition)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectedPosition = 0
        arguments?.let {
            product = it.getParcelable(PRODUCT)
            isThumbnailMode = it.getBoolean(IS_THUMBNAIL_MODE)
            selectedPosition = it.getInt(SELECTED_POSITION)
        }

        setupAdapter()
        pager.adapter = adapter
        product?.let { setData() }
        pager.currentItem = selectedPosition
        indicator.setViewPager(pager)
    }

    private fun setupAdapter() {
        adapter = GalleryPagerAdapter(childFragmentManager)
        adapter.registerDataSetObserver(indicator.dataSetObserver)
        adapter.imageClickListener = if (isThumbnailMode) {
            View.OnClickListener {
                context?.let {
                    startActivity(GalleryActivity.getStartIntent(it, product, pager.currentItem))
                }
            }
        } else {
            imageClickListener
        }
    }

    private fun setData() {
        adapter.product = product
        adapter.isThumbnailMode = isThumbnailMode
        adapter.notifyDataSetChanged()
        indicator.visibility = if (product?.images?.size ?: 0 <= 1) View.INVISIBLE else View.VISIBLE
    }

    fun updateProduct(product: Product) {
        this.product = product
        setData()
    }
}