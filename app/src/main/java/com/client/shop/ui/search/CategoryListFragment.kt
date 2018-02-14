package com.client.shop.ui.search

import android.os.Bundle
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.getaway.entity.Category
import com.client.shop.ui.base.pagination.PaginationFragment
import com.client.shop.ui.category.CategoryActivity
import com.client.shop.ui.home.adapter.CategoriesAdapter
import com.client.shop.ui.search.contract.CategoryListPresenter
import com.client.shop.ui.search.contract.CategoryListView
import com.client.shop.ui.search.di.SearchModule
import javax.inject.Inject

class CategoryListFragment :
    PaginationFragment<Category, CategoryListView, CategoryListPresenter>(),
    CategoryListView {

    @Inject
    lateinit var categoryListPresenter: CategoryListPresenter

    //ANDROID

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData(true)
    }

    //INIT

    override fun getContentView() = R.layout.fragment_search_with_categories_list

    override fun inject() {
        ShopApplication.appComponent.attachSearchComponent(SearchModule()).inject(this)
    }

    override fun createPresenter() = categoryListPresenter

    //SETUP

    override fun isGrid() = true

    override fun setupAdapter() = CategoriesAdapter(dataList, this)

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.getCategoryList(perPageCount(), paginationValue)
    }

    override fun showContent(data: List<Category>) {
        super.showContent(data)
        dataList.addAll(data)
        adapter.notifyDataSetChanged()
        data.lastOrNull()?.let { paginationValue = it.paginationValue }
    }

    //CALLBACK

    override fun onItemClicked(data: Category, position: Int) {
        context?.let {
            startActivity(CategoryActivity.getStartIntent(it, data))
        }
    }
}