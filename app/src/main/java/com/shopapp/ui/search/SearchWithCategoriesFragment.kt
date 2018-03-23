package com.shopapp.ui.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shopapp.R
import com.shopapp.ui.category.CategoryListFragment
import kotlinx.android.synthetic.main.fragment_search_with_categories.*

class SearchWithCategoriesFragment : Fragment(), SearchToolbar.SearchToolbarListener {

    private val searchFragment: SearchFragment by lazy {
        SearchFragment()
    }
    private val categoriesFragment: CategoryListFragment by lazy {
        CategoryListFragment()
    }

    companion object {
        enum class ContentType {
            Search,
            Categories
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_with_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchToolbar.searchToolbarListener = this
        changeContent(ContentType.Categories)
    }

    private fun changeContent(contentType: ContentType) {
        val fragment = when (contentType) {
            ContentType.Search -> searchFragment
            ContentType.Categories -> categoriesFragment
        }
        childFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.container, fragment)
            .commit()
    }

    /**
     * @return 'true' if onBackPressed allowed
     */
    fun onBackPressed(): Boolean {
        return if (searchToolbar.isToolbarExpanded()) {
            searchToolbar.changeToolbarState()
            false
        } else {
            true
        }
    }

    override fun onQueryChanged(query: String) {
        if (searchFragment.isVisible) {
            searchFragment.queryChanged(query)
        }
    }

    override fun onToolbarStateChanged(isExpanded: Boolean) {
        changeContent(if (isExpanded) ContentType.Search else ContentType.Categories)
    }
}