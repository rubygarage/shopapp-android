package com.shopapp.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.shopapp.R
import com.shopapp.ext.replaceOnce
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.base.ui.FragmentVisibilityListener
import com.shopapp.ui.blog.BlogFragment
import com.shopapp.ui.product.ProductHorizontalFragment
import com.shopapp.ui.product.ProductPopularFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment :
    Fragment(),
    SwipeRefreshLayout.OnRefreshListener {

    private var productHorizontalFragment: ProductHorizontalFragment? = null
    private var popularFragment: ProductPopularFragment? = null
    private var blogFragment: BlogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout.setOnRefreshListener(this)


        childFragmentManager.replaceOnce(
            R.id.recentContainer,
            ProductHorizontalFragment::javaClass.name,
            {
                val fragment = ProductHorizontalFragment.newInstance(SortType.RECENT)
                productHorizontalFragment = fragment
                productHorizontalFragment?.visibilityListener = object : FragmentVisibilityListener {
                    override fun changeVisibility(isVisible: Boolean) {
                        recentContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
                    }
                }
                fragment
            }
        ).commit()

        childFragmentManager.replaceOnce(
            R.id.popularContainer,
            ProductPopularFragment::javaClass.name,
            {
                val fragment = ProductPopularFragment()
                popularFragment = fragment
                popularFragment?.visibilityListener = object : FragmentVisibilityListener {
                    override fun changeVisibility(isVisible: Boolean) {
                        popularContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
                    }
                }
                fragment
            }
        ).commit()

        childFragmentManager.replaceOnce(
            R.id.blogContainer,
            BlogFragment::javaClass.name,
            {
                val fragment = BlogFragment()
                blogFragment = fragment
                blogFragment?.visibilityListener = object : FragmentVisibilityListener {
                    override fun changeVisibility(isVisible: Boolean) {
                        blogContainer.visibility = if (isVisible) View.VISIBLE else View.GONE
                    }
                }
                fragment
            }
        ).commit()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val compatActivity = activity
        if (compatActivity is AppCompatActivity) {
            toolbar.setTitle(getString(R.string.shop))
            compatActivity.setSupportActionBar(toolbar)
            compatActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onRefresh() {
        productHorizontalFragment?.loadData(true)
        popularFragment?.loadData(true)
        blogFragment?.loadData(true)
        refreshLayout.isRefreshing = false
    }
}