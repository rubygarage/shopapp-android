package com.shopapp.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ext.replaceOnce
import com.shopapp.gateway.entity.Config
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.base.lce.BaseLceFragment
import com.shopapp.ui.base.ui.FragmentVisibilityListener
import com.shopapp.ui.blog.BlogFragment
import com.shopapp.ui.product.ProductPopularFragment
import com.shopapp.ui.product.ProductShortcutFragment
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var config: Config

    private var productShortcutFragment: ProductShortcutFragment? = null
    private var popularFragment: ProductPopularFragment? = null
    private var blogFragment: BlogFragment? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        ShopApplication.appComponent.attachHomeComponent().inject(this)
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

        addChildFragment(true, recentContainer, {
            ProductShortcutFragment.newInstance(
                SortType.RECENT,
                isHorizontalMode = config.isBlogEnabled || config.isPopularEnabled
            )
        })
        addChildFragment(config.isPopularEnabled, popularContainer, { ProductPopularFragment() })
        addChildFragment(config.isBlogEnabled, blogContainer, { BlogFragment() })
    }

    private fun addChildFragment(
        isFragmentAllowed: Boolean,
        container: View,
        creator: () -> BaseLceFragment<*, *, *>
    ) {
        if (isFragmentAllowed) {
            val containerId = container.id
            val fragment = childFragmentManager.replaceOnce(
                containerViewId = containerId,
                fragmentTag = containerId.toString(),
                creator = creator
            )
            when (fragment) {
                is ProductShortcutFragment -> productShortcutFragment = fragment
                is ProductPopularFragment -> popularFragment = fragment
                is BlogFragment -> blogFragment = fragment
            }
            (fragment as? BaseLceFragment<*, *, *>)?.fragmentVisibilityListener =
                    object : FragmentVisibilityListener {
                        override fun changeVisibility(isVisible: Boolean) {
                            refreshLayout.isRefreshing = false
                            container.visibility =
                                    if (isVisible) View.VISIBLE else View.GONE
                        }
                    }
        } else {
            container.visibility = View.GONE
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

    //CALLBACK

    override fun onRefresh() {
        productShortcutFragment?.loadData(true)
        popularFragment?.loadData(true)
        blogFragment?.loadData(true)
    }
}