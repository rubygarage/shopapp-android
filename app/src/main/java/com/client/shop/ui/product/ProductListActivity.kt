package com.client.shop.ui.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.ui.pagination.PaginationActivity
import com.client.shop.ui.product.adapter.ProductListAdapter
import com.client.shop.ui.product.contract.ProductListPresenter
import com.client.shop.ui.product.contract.ProductListView
import com.client.shop.ui.product.di.ProductListModule
import com.domain.entity.Product
import com.domain.entity.SortType
import com.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import javax.inject.Inject

class ProductListActivity :
        PaginationActivity<Product, ProductListView, ProductListPresenter>(),
        ProductListView {

    companion object {

        private const val TITLE = "title"
        private const val SORT_TYPE = "sort_type"
        private const val KEY_PHRASE = "key_phrase"

        fun getStartIntent(context: Context, title: String, sortType: SortType = SortType.NAME,
                           keyPhrase: String? = null): Intent {
            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtra(TITLE, title)
            intent.putExtra(SORT_TYPE, sortType)
            intent.putExtra(KEY_PHRASE, keyPhrase)
            return intent
        }
    }

    @Inject lateinit var productListPresenter: ProductListPresenter
    private var keyPhrase: String? = null
    private lateinit var sortType: SortType

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sortType = intent.getSerializableExtra(SORT_TYPE) as SortType
        setTitle(intent.getStringExtra(TITLE))
        keyPhrase = intent.getStringExtra(KEY_PHRASE)
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachProductComponent(ProductListModule()).inject(this)
    }

    override fun createPresenter() = productListPresenter

    override fun isGrid() = true

    //SETUP

    override fun setupAdapter(): ProductListAdapter {
        val size = resources.getDimensionPixelSize(R.dimen.product_item_size)
        return ProductListAdapter(ViewGroup.LayoutParams.MATCH_PARENT, size, dataList, this)
    }

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        recycler.setBackgroundResource(R.color.white)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadProductList(sortType, DEFAULT_PER_PAGE_COUNT, paginationValue, keyPhrase)
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