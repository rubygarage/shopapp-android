package com.shopapp.ui.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.ViewGroup
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.base.lce.view.LceEmptyView
import com.shopapp.ui.base.pagination.PaginationActivity
import com.shopapp.ui.category.contract.CategoryPresenter
import com.shopapp.ui.category.contract.CategoryView
import com.shopapp.ui.category.router.CategoryRouter
import com.shopapp.ui.const.Constant
import com.shopapp.ui.product.adapter.ProductListAdapter
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import javax.inject.Inject


class CategoryActivity :
    PaginationActivity<Product, CategoryView, CategoryPresenter>(),
    CategoryView,
    CategorySortPopupFacade.OnSortTypeChangeListener {

    @Inject
    lateinit var categoryPresenter: CategoryPresenter

    @Inject
    lateinit var router: CategoryRouter

    private lateinit var category: Category
    private var sortType: SortType = SortType.NAME
    private var positiveScrollOffset = 0
    private var negativeScrollOffset = 0
    private var isCollapsed = false
    private var categorySortPopupFacade: CategorySortPopupFacade? = null

    companion object {

        private const val PROGRESS_START_BIAS = 0.2f
        private const val PROGRESS_END_BIAS = 1.5f
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"

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
        lceLayout.errorView.errorTarget = getString(R.string.category)

        categorySortPopupFacade = CategorySortPopupFacade(this, this)
        sortLayout.setOnClickListener { categorySortPopupFacade?.showSortPopup(it, sortType) }
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachCategoryComponent().inject(this)
    }

    override fun createPresenter() = categoryPresenter

    override fun isGrid() = true

    override fun getContentView() = R.layout.activity_category

    //SETUP

    override fun setupAdapter(): ProductListAdapter {
        val size = resources.getDimensionPixelSize(R.dimen.product_grid_item_size)
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

    override fun setupSwipeRefreshLayout() {
        super.setupSwipeRefreshLayout()
        val sortViewHeight = resources.getDimensionPixelSize(R.dimen.sort_view_height)
        val start = (sortViewHeight * PROGRESS_START_BIAS).toInt()
        val end = (sortViewHeight * PROGRESS_END_BIAS).toInt()
        swipeRefreshLayout.setProgressViewOffset(true, start, end)
        swipeRefreshLayout.setProgressViewEndTarget(true, end - start)
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
        paginationValue = data.last().paginationValue
        this.dataList.addAll(data)
        adapter.notifyDataSetChanged()
    }

    //CALLBACK

    override fun onItemClicked(data: Product, position: Int) {
        router.showProduct(this, data.id)
    }

    override fun onSortTypeChanged(sortType: SortType) {
        this.sortType = sortType
        onRefresh()
    }
}