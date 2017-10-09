package com.client.shop.ui.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.client.shop.R
import com.client.shop.ui.blog.BlogFragment
import com.client.shop.ui.recent.RecentFragment
import com.client.shop.ui.search.SearchActivity

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction().replace(R.id.recentContainer, RecentFragment()).commit()
        childFragmentManager.beginTransaction().replace(R.id.blogContainer, BlogFragment()).commit()

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        item?.let {
            if (item.itemId == R.id.search) {
                startActivity(SearchActivity.getStartIntent(context))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}