package com.client.shop.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.client.shop.R
import com.client.shop.ui.blog.BlogFragment
import com.client.shop.ui.popular.PopularFragment
import com.client.shop.ui.product.ProductHorizontalFragment
import com.domain.entity.SortType
import com.ui.ext.replaceOnce
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment :
    Fragment(),
    SwipeRefreshLayout.OnRefreshListener {

    private var productHorizontalFragment: ProductHorizontalFragment? = null
    private var popularFragment: PopularFragment? = null
    private var blogFragment: BlogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout.setOnRefreshListener(this)

        childFragmentManager.replaceOnce(R.id.recentContainer, ProductHorizontalFragment::javaClass.name,
            {
                val fragment = ProductHorizontalFragment.newInstance(SortType.RECENT)
                productHorizontalFragment = fragment
                fragment
            }).commit()
        childFragmentManager.replaceOnce(R.id.popularContainer, PopularFragment::javaClass.name,
            {
                val fragment = PopularFragment()
                popularFragment = fragment
                fragment
            }).commit()
        childFragmentManager.replaceOnce(R.id.blogContainer, BlogFragment::javaClass.name,
            {
                val fragment = BlogFragment()
                blogFragment = fragment
                fragment
            }).commit()
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