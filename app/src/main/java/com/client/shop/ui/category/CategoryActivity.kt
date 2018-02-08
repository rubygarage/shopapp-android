package com.client.shop.ui.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.ui.pagination.PaginationActivity
import com.client.shop.ui.category.contract.CategoryPresenter
import com.client.shop.ui.category.contract.CategoryView
import com.client.shop.ui.category.di.CategoryModule
import com.client.shop.ui.product.ProductDetailsActivity
import com.client.shop.ui.product.adapter.ProductListAdapter
import com.domain.entity.Category
import com.domain.entity.Product
import com.domain.entity.SortType
import com.ui.base.lce.view.LceEmptyView
import com.ui.const.Constant
import kotlinx.android.synthetic.main.activity_category.*
import javax.inject.Inject


class CategoryActivity :
    PaginationActivity<Product, CategoryView, CategoryPresenter>(),
    CategoryView,
    CategorySortPopupFacade.OnSortTypeChangeListener {

    @Inject
    lateinit var categoryPresenter: CategoryPresenter
    private lateinit var category: Category
    private var sortType: SortType = SortType.NAME
    private var positiveScrollOffset = 0
    private var negativeScrollOffset = 0
    private var isCollapsed = false
    private var categorySortPopupFacade: CategorySortPopupFacade? = null

    companion object {

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
        categorySortPopupFacade = CategorySortPopupFacade(this, this)
        sortLayout.setOnClickListener { categorySortPopupFacade?.showSortPopup(it, sortType) }
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_categories, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == R.id.sort) {
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

    override fun getContentView() = R.layout.activity_category

    //SETUP

    override fun setupAdapter(): ProductListAdapter {
        val size = resources.getDimensionPixelSize(R.dimen.product_item_size)
        return ProductListAdapter(ViewGroup.LayoutParams.MATCH_PARENT, size, dataList, this)
    }

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        val scrollOffset = resources.getDimensionPixelSize(R.dimen.sort_view_height)
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    positiveScrollOffset += dy
                    negativeScrollOffset = 0
                } else {
                    negativeScrollOffset += dy
                    positiveScrollOffset = 0
                }

                if (positiveScrollOffset >= scrollOffset && !isCollapsed) {
                    changeSortLayoutState(-sortLayout.height.toFloat())
                } else if (Math.abs(negativeScrollOffset) >= scrollOffset && isCollapsed) {
                    changeSortLayoutState(0f)
                }
            }
        })
    }

    override fun setupEmptyView(emptyView: LceEmptyView) {
        emptyView.customiseEmptyImage(R.drawable.ic_categories_empty)
        emptyView.customiseEmptyMessage(R.string.there_are_no_products_yet)
    }

    private fun changeSortLayoutState(destination: Float) {
        sortLayout.animate().translationY(destination)
        isCollapsed = !isCollapsed
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadProductList(Constant.DEFAULT_PER_PAGE_COUNT, paginationValue, category.id, sortType)
    }

    override fun showContent(data: List<Product>) {
        super.showContent(data)
        if (data.isNotEmpty()) {
            paginationValue = data.last().paginationValue
            this.dataList.addAll(data)
            adapter.notifyDataSetChanged()
        }
        if (dataList.isEmpty()) {
            showEmptyState()
        }
    }

    //CALLBACK

    override fun onItemClicked(data: Product, position: Int) {
        startActivity(ProductDetailsActivity.getStartIntent(this, data.id))
    }

    override fun onSortTypeChanged(sortType: SortType) {
        this.sortType = sortType
        onRefresh()
    }
}