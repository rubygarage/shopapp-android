package com.client.shop.ui.recent

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.const.Constant.DEFAULT_PER_PAGE_COUNT
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.product.ProductListActivity
import com.client.shop.ui.product.adapter.ProductAdapter
import com.client.shop.ui.product.contract.ProductListPresenter
import com.client.shop.ui.product.contract.ProductListView
import com.client.shop.ui.recent.di.RecentModule
import com.domain.entity.Product
import com.domain.entity.SortType
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.ui.base.lce.BaseFragment
import com.ui.base.recycler.OnItemClickListener
import com.ui.base.recycler.divider.SpaceDecoration
import kotlinx.android.synthetic.main.fragment_recent.*
import javax.inject.Inject

class RecentFragment :
        BaseFragment<List<Product>, ProductListView, ProductListPresenter>(),
        ProductListView,
        OnItemClickListener {

    @Inject lateinit var productListPresenter: ProductListPresenter
    private val productList = mutableListOf<Product>()
    private val sortType = SortType.RECENT
    private lateinit var adapter: ProductAdapter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seeAll.setOnClickListener {
            startActivity(ProductListActivity.getStartIntent(context,
                    getString(R.string.latest_arrivals), sortType))
        }
        changeSeeAllState()
        setupRecycler()
        loadData(true)
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachRecentComponent(RecentModule()).inject(this)
    }

    override fun getContentView() = R.layout.fragment_recent

    override fun createPresenter() = productListPresenter

    //SETUP

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val size = resources.getDimensionPixelSize(R.dimen.product_item_size)
        adapter = ProductAdapter(size, size, productList, this)
        GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView)
        val decoration = SpaceDecoration(leftSpace = resources.getDimensionPixelSize(R.dimen.content_space))
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(decoration)
    }

    private fun changeSeeAllState() {
        if (productList.size == DEFAULT_PER_PAGE_COUNT) {
            seeAll.visibility = View.VISIBLE
        } else {
            seeAll.visibility = View.GONE
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadProductList(sortType, DEFAULT_PER_PAGE_COUNT)
    }

    override fun showContent(data: List<Product>) {
        super.showContent(data)
        productList.clear()
        productList.addAll(data)
        adapter.notifyDataSetChanged()
        changeSeeAllState()
    }

    //CALLBACK

    override fun onItemClicked(position: Int) {
        productList.getOrNull(position)?.let {
            startActivity(DetailsActivity.getStartIntent(context, it.id))
        }
    }
}