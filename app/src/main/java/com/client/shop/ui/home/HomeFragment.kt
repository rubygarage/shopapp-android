package com.client.shop.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.client.shop.R
import com.ui.ext.replaceOnce
import com.client.shop.ui.blog.BlogFragment
import com.client.shop.ui.popular.PopularFragment
import com.client.shop.ui.recent.RecentFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.replaceOnce(R.id.recentContainer, RecentFragment::javaClass.name, { RecentFragment() }).commit()
        childFragmentManager.replaceOnce(R.id.popularContainer, PopularFragment::javaClass.name, {   PopularFragment() }).commit()
        childFragmentManager.replaceOnce(R.id.blogContainer, BlogFragment::javaClass.name, { BlogFragment() }).commit()
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

}