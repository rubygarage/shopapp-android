package com.client.shop.ui.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.pagination.PaginationActivity
import com.client.shop.ui.product.adapter.ProductListAdapter
import com.client.shop.ui.product.contract.ProductListPresenter
import com.client.shop.ui.product.contract.ProductListView
import com.client.shop.ui.product.di.ProductListModule
import com.client.shop.getaway.entity.Product
import com.client.shop.getaway.entity.SortType
import com.client.shop.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import javax.inject.Inject

class ProductListActivity :
    PaginationActivity<Product, ProductListView, ProductListPresenter>(),
    ProductListView {

    companion object {

        private const val TITLE = "title"
        private const val SORT_TYPE = "sort_type"
        private const val KEYWORD = "keyword"
        private const val EXCLUDE_KEYWORD = "exclude_keyword"

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
        startActivity(ProductDetailsActivity.getStartIntent(this, data.id))
    }
}