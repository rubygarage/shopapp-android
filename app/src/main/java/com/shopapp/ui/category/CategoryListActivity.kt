package com.shopapp.ui.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Category
import com.shopapp.ui.base.pagination.PaginationActivity
import com.shopapp.ui.category.adapter.CategoryListAdapter
import com.shopapp.ui.category.contract.CategoryListPresenter
import com.shopapp.ui.category.contract.CategoryListView
import com.shopapp.ui.category.router.CategoryListRouter
import javax.inject.Inject

class CategoryListActivity : PaginationActivity<Category, CategoryListView, CategoryListPresenter>(),
    CategoryListView {

    companion object {

        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"

        fun getStartIntent(context: Context, category: Category): Intent {
            val intent = Intent(context, CategoryListActivity::class.java)
            intent.putExtra(EXTRA_CATEGORY, category)
            return intent
        }
    }

    @Inject
    lateinit var categoryListPresenter: CategoryListPresenter

    @Inject
    lateinit var router: CategoryListRouter

    lateinit var rootCategory: Category

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootCategory = intent.getParcelableExtra(EXTRA_CATEGORY)
        loadData()
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachCategoryComponent().inject(this)
    }

    override fun getContentView() = R.layout.fragment_search_with_categories_list

    override fun createPresenter() = categoryListPresenter

    //SETUP

    override fun setupAdapter() = CategoryListAdapter(dataList, this, isGrid())

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.getCategoryList(perPageCount(), paginationValue, rootCategory.id)
    }

    override fun showContent(data: List<Category>) {
        super.showContent(data)
        dataList.addAll(data)
        adapter.notifyDataSetChanged()
        data.lastOrNull()?.let { paginationValue = it.paginationValue }
    }

    //CALLBACK
    override fun onItemClicked(data: Category, position: Int) {
        if (data.childrenCategoryList.isEmpty()) {
            router.showCategory(this, data)
        } else {
            router.showCategoryList(this, data)
        }
    }
}