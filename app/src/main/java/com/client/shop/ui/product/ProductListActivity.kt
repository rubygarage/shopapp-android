package com.client.shop.ui.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.const.Constant.DEFAULT_PER_PAGE_COUNT
import com.client.shop.ui.base.ui.pagination.PaginationActivity
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.product.adapter.ProductAdapter
import com.client.shop.ui.product.contract.ProductListPresenter
import com.client.shop.ui.product.contract.ProductListView
import com.client.shop.ui.product.di.ProductModule
import com.domain.entity.Product
import com.domain.entity.SortType
import kotlinx.android.synthetic.main.activity_pagination.*
import javax.inject.Inject

class ProductListActivity :
        PaginationActivity<Product, ProductListView, ProductListPresenter>(),
        ProductListView {

    @Inject lateinit var productListPresenter: ProductListPresenter
    private lateinit var sortType: SortType

    companion object {

        private const val TITLE = "title"
        private const val SORT_TYPE = "SORT_TYPE"

        fun getStartIntent(context: Context, title: String, sortType: SortType = SortType.NAME): Intent {
            val intent = Intent(context, ProductListActivity::class.java)
            intent.putExtra(TITLE, title)
            intent.putExtra(SORT_TYPE, sortType)
            return intent
        }
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sortType = intent.getSerializableExtra(SORT_TYPE) as SortType
        setTitle(intent.getStringExtra(TITLE))
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachProductComponent(ProductModule()).inject(this)
    }

    override fun createPresenter() = productListPresenter

    override fun isGrid() = true

    //SETUP

    override fun setupAdapter(): ProductAdapter {
        val size = resources.getDimensionPixelSize(R.dimen.product_item_size)
        return ProductAdapter(ViewGroup.LayoutParams.MATCH_PARENT, size, dataList, this)
    }

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        recyclerView.setBackgroundResource(R.color.white)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadProductList(sortType, DEFAULT_PER_PAGE_COUNT, paginationValue)
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
        startActivity(DetailsActivity.getStartIntent(this, data.id))
    }
}