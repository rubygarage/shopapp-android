package com.client.shop.ui.category

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.ui.PaginationFragment
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.category.adapter.CategoryAdapter
import com.client.shop.ui.category.contract.CategoryPresenter
import com.client.shop.ui.category.contract.CategoryView
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.modal.SortBottomSheet
import com.shopapicore.entity.Category
import com.shopapicore.entity.Product
import com.shopapicore.entity.SortType
import javax.inject.Inject

class CategoryFragment : PaginationFragment<Product, CategoryView, CategoryPresenter, CategoryViewState>(),
        CategoryView {

    @Inject lateinit var categoryPresenter: CategoryPresenter
    private lateinit var category: Category
    private var selectedSortType: SortType = SortType.NAME

    private val sortBottomSheet: SortBottomSheet by lazy {
        SortBottomSheet(context, object : OnItemClickListener<SortType> {
            override fun onItemClicked(data: SortType, position: Int) {
                if (selectedSortType != data) {
                    selectedSortType = data
                    onRefresh()
                }
            }
        })
    }

    companion object {

        private const val PER_PAGE = 10
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"

        fun newInstance(category: Category): CategoryFragment {
            val fragment = CategoryFragment()
            val args = Bundle()
            args.putParcelable(EXTRA_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        category = arguments.getParcelable(EXTRA_CATEGORY)
        val compatActivity = activity
        if (compatActivity is AppCompatActivity) {
            compatActivity.supportActionBar?.setDisplayShowTitleEnabled(true)
            compatActivity.supportActionBar?.title = category.title
        }
        setHasOptionsMenu(true)
    }

    override fun inject(component: AppComponent) {
        component.inject(this)
    }

    override fun createPresenter() = categoryPresenter

    override fun isGrid() = true

    override fun initAdapter() = CategoryAdapter(dataList, this)

    override fun createViewState() = CategoryViewState()

    override fun onNewViewStateInstance() {
        fetchData()
    }

    override fun fetchData() {
        presenter.loadProductList(PER_PAGE, paginationValue, category.id, selectedSortType)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_categories, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.sort) {
            sortBottomSheet.show(selectedSortType)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClicked(data: Product, position: Int) {
        startActivity(DetailsActivity.getStartIntent(context, data))
    }

    override fun productListLoaded(productList: List<Product>?) {
        val size = productList?.size ?: 0
        if (productList != null && size > 0) {
            paginationValue = productList.last().paginationValue
            this.dataList.addAll(productList)
            adapter?.notifyDataSetChanged()
        }
        viewState.setData(dataList)
    }
}