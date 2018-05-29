package com.shopapp.ui.home

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Config
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.base.lce.BaseLceFragment
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.base.ui.FragmentVisibilityListener
import com.shopapp.ui.blog.BlogFragment
import com.shopapp.ui.home.contract.HomePresenter
import com.shopapp.ui.home.contract.HomeView
import com.shopapp.ui.product.ProductPopularFragment
import com.shopapp.ui.product.ProductShortcutFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_lce.*
import javax.inject.Inject

class HomeFragment :
    BaseLceFragment<Unit, HomeView, HomePresenter>(),
    HomeView,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var homePresenter: HomePresenter
    private var productShortcutFragment: ProductShortcutFragment? = null
    private var popularFragment: ProductPopularFragment? = null
    private var blogFragment: BlogFragment? = null
    private var config: Config? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout.setOnRefreshListener(this)

        config?.let {
            changeChildVisibility(it.isPopularEnabled, popularContainer)
            changeChildVisibility(it.isBlogEnabled, blogContainer)
        } ?: also {
            showLoading()
            presenter.getConfig()
        }
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

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachHomeComponent().inject(this)
    }

    override fun getRootView() = R.layout.layout_lce

    override fun getContentView() = R.layout.fragment_home

    override fun createPresenter() = homePresenter

    //LCE

    override fun onConfigReceived(config: Config) {
        if (this.config == null) {
            this.config = config

            addChildFragment(true, recentContainer, {
                val childFragment = ProductShortcutFragment.newInstance(
                    sortType = SortType.RECENT,
                    isHorizontalMode = config.isBlogEnabled || config.isPopularEnabled
                )
                productShortcutFragment = childFragment
                childFragment
            })

            addChildFragment(config.isPopularEnabled, popularContainer, {
                val childFragment = ProductPopularFragment()
                popularFragment = childFragment
                childFragment
            })

            addChildFragment(config.isBlogEnabled, blogContainer, {
                val childFragment = BlogFragment()
                blogFragment = childFragment
                childFragment
            })
        }
    }

    private fun addChildFragment(
        isFragmentAllowed: Boolean,
        childContainer: View,
        fragmentCreator: () -> BaseLceFragment<*, *, *>) {
        if (isFragmentAllowed) {
            val childFragment = fragmentCreator()
            childFragment.fragmentVisibilityListener = object : FragmentVisibilityListener {
                override fun changeVisibility(isVisible: Boolean) {
                    lceLayout.changeState(LceLayout.LceState.ContentState)
                    if (isVisible) {
                        mainContainer.visibility = VISIBLE
                    }
                    refreshLayout.isRefreshing = false
                    changeChildVisibility(isVisible, childContainer)
                }
            }
            childFragmentManager.beginTransaction()
                .replace(childContainer.id, childFragment)
                .commit()
        } else {
            changeChildVisibility(isFragmentAllowed, childContainer)
        }
    }

    private fun changeChildVisibility(isFragmentAllowed: Boolean, childContainer: View) {
        childContainer.visibility = if (isFragmentAllowed) VISIBLE else GONE
    }

    //CALLBACK

    override fun onRefresh() {
        productShortcutFragment?.loadData(true)
        popularFragment?.loadData(true)
        blogFragment?.loadData(true)
    }
}