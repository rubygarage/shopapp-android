package com.shopapp.ui.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.base.pagination.PaginationActivity
import com.shopapp.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import com.shopapp.ui.product.adapter.ProductListAdapter
import com.shopapp.ui.product.contract.ProductListPresenter
import com.shopapp.ui.product.contract.ProductListView
import com.shopapp.ui.product.router.ProductRouter
import javax.inject.Inject

class ProductListActivity :
    PaginationActivity<Product, ProductListView, ProductListPresenter>(),
    ProductListView {

    companion object {

        const val TITLE = "title"
        const val SORT_TYPE = "sort_type"
        const val KEYWORD = "keyword"
        const val EXCLUDE_KEYWORD = "exclude_keyword"

        fun getStartIntent(context: Context, title: String, sortType: SortType = SortType.NAME,
                           keyword: String? = null, excludeKeyword: String? = null): Intent {
            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtra(TITLE, title)
            intent.putExtra(SORT_TYPE, sortType)
            intent.putExtra(KEYWORD, keyword)
            intent.putExtra(EXCLUDE_KEYWORD, excludeKeyword)
            return intent
        }
    }

    @Inject
    lateinit var productListPresenter: ProductListPresenter

    @Inject
    lateinit var router: ProductRouter

    private var keyword: String? = null
    private var excludeKeyword: String? = null
    private lateinit var sortType: SortType

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sortType = intent.getSerializableExtra(SORT_TYPE) as SortType
        setTitle(intent.getStringExtra(TITLE))
        keyword = intent.getStringExtra(KEYWORD)
        excludeKeyword = intent.getStringExtra(EXCLUDE_KEYWORD)
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachProductComponent().inject(this)
    }

    override fun createPresenter() = productListPresenter

    override fun isGrid() = true

    //SETUP

    override fun setupAdapter(): ProductListAdapter {
        val size = resources.getDimensionPixelSize(R.dimen.product_grid_item_size)
        return ProductListAdapter(ViewGroup.LayoutParams.MATCH_PARENT, size, dataList, this)
    }

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        recycler.setBackgroundResource(R.color.white)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadProductList(sortType, DEFAULT_PER_PAGE_COUNT, paginationValue, keyword, excludeKeyword)
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
        router.showProduct(this, data.id)
    }
}