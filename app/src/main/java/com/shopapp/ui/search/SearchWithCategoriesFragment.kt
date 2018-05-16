package com.shopapp.ui.search

import android.os.Bundle
import android.view.View
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Config
import com.shopapp.ui.base.lce.BaseLceFragment
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.category.CategoryListFragment
import com.shopapp.ui.search.contract.SearchWithCategoriesPresenter
import com.shopapp.ui.search.contract.SearchWithCategoriesView
import kotlinx.android.synthetic.main.fragment_search_with_categories_lce.*
import javax.inject.Inject

class SearchWithCategoriesFragment :
    BaseLceFragment<Config, SearchWithCategoriesView, SearchWithCategoriesPresenter>(),
    SearchWithCategoriesView,
    SearchToolbar.SearchToolbarListener {

    @Inject
    lateinit var searchWithCategoriesPresenter: SearchWithCategoriesPresenter
    private var config: Config? = null

    private val searchFragment: SearchFragment by lazy {
        SearchFragment()
    }
    private val categoriesFragment: CategoryListFragment by lazy {
        CategoryListFragment.newInstance(null, config?.isCategoryGridEnabled)
    }

    companion object {
        enum class ContentType {
            Search,
            Categories
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchToolbar.searchToolbarListener = this
        config ?: also {
            lceLayout.changeState(LceLayout.LceState.LoadingState(useDelay = true))
            presenter.getConfig()
        }
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

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachSearchComponent().inject(this)
    }

    override fun getRootView() = R.layout.fragment_search_with_categories_lce

    override fun getContentView() = R.layout.fragment_search_with_categories

    override fun createPresenter() = searchWithCategoriesPresenter

    //LCE

    override fun showContent(data: Config) {
        super.showContent(data)
        config = data
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

    //CALLBACK

    override fun onQueryChanged(query: String) {
        if (searchFragment.isVisible || query.isBlank()) {
            searchFragment.queryChanged(query)
        }
    }

    override fun onToolbarStateChanged(isExpanded: Boolean) {
        changeContent(if (isExpanded) ContentType.Search else ContentType.Categories)
    }
}