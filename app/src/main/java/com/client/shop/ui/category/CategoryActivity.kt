package com.client.shop.ui.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.ui.pagination.PaginationActivity
import com.client.shop.ui.category.contract.CategoryPresenter
import com.client.shop.ui.category.contract.CategoryView
import com.client.shop.ui.category.di.CategoryModule
import com.client.shop.ui.modal.SortBottomSheet
import com.client.shop.ui.product.ProductDetailsActivity
import com.client.shop.ui.product.adapter.ProductListAdapter
import com.domain.entity.Category
import com.domain.entity.Product
import com.domain.entity.SortType
import com.ui.base.recycler.divider.BackgroundItemDecoration
import javax.inject.Inject

class CategoryActivity : PaginationActivity<Product, CategoryView, CategoryPresenter>(),
    CategoryView {

    @Inject
    lateinit var categoryPresenter: CategoryPresenter
    private lateinit var category: Category
    private var sortType: SortType = SortType.NAME

    private val sortBottomSheet: SortBottomSheet by lazy {
        SortBottomSheet(this, object : SortBottomSheet.OnSortTypeSelectListener {

            override fun onSortTypeSelected(selectedSortType: SortType) {
                if (sortType != selectedSortType) {
                    sortType = selectedSortType
                    onRefresh()
                }
            }
        })
    }

    companion object {

        private const val PER_PAGE = 10
        private const val EXTRA_CATEGORY = "EXTRA_CATEGORY"

        fun getStartIntent(context: Context, category: Category): Intent {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra(EXTRA_CATEGORY, category)
            return intent
        }
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = intent.getParcelableExtra(EXTRA_CATEGORY)
        setTitle(category.title)
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_categories, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.sort) {
            sortBottomSheet.show(sortType)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachCategoryComponent(CategoryModule()).inject(this)
    }

    override fun createPresenter() = categoryPresenter

    override fun isGrid() = true

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        recycler.addItemDecoration(BackgroundItemDecoration(R.color.white))
    }

    //SETUP

    override fun setupAdapter(): ProductListAdapter {
        val size = resources.getDimensionPixelSize(R.dimen.product_item_size)
        return ProductListAdapter(ViewGroup.LayoutParams.MATCH_PARENT, size, dataList, this)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadProductList(PER_PAGE, paginationValue, category.id, sortType)
    }

    override fun showContent(data: List<Product>) {
        super.showContent(data)
        if (data.isNotEmpty()) {
            paginationValue = data.last().paginationValue
            this.dataList.addAll(data)
            adapter.notifyDataSetChanged()
        }
    }

    //CALLBACK

    override fun onItemClicked(data: Product, position: Int) {
        startActivity(ProductDetailsActivity.getStartIntent(this, data.id))
    }
}