package com.shopapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Category
import com.shopapp.ui.base.pagination.PaginationFragment
import com.shopapp.ui.category.adapter.CategoryListAdapter
import com.shopapp.ui.category.contract.CategoryListPresenter
import com.shopapp.ui.category.contract.CategoryListView
import com.shopapp.ui.category.router.CategoryListRouter
import javax.inject.Inject

class CategoryListFragment :
    PaginationFragment<Category, CategoryListView, CategoryListPresenter>(),
    CategoryListView {

    companion object {

        private const val PARENT_CATEGORY = "parent_category"

        fun newInstance(parentCategory: Category?): CategoryListFragment {
            val fragment = CategoryListFragment()
            val args = Bundle()
            args.putParcelable(PARENT_CATEGORY, parentCategory)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var categoryListPresenter: CategoryListPresenter

    @Inject
    lateinit var router: CategoryListRouter

    private var parentCategory: Category? = null

    //ANDROID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            parentCategory = it.getParcelable(PARENT_CATEGORY)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dataList.isEmpty()) {
            loadData(true)
        }
    }

    //INIT

    override fun getContentView() = R.layout.fragment_search_with_categories_list

    override fun inject() {
        ShopApplication.appComponent.attachCategoryComponent().inject(this)
    }

    override fun createPresenter() = categoryListPresenter

    //SETUP

    override fun isGrid() = true

    override fun setupAdapter() = CategoryListAdapter(dataList, this)

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.getCategoryList(perPageCount(), paginationValue, parentCategory?.id)
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
            router.showCategory(context, data)
        } else {
            router.showCategoryList(context, data)
        }
    }
}