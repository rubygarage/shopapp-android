package com.client.shop.ui.popular

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.product.ProductDetailsActivity
import com.client.shop.ui.popular.di.PopularModule
import com.client.shop.ui.product.ProductListActivity
import com.client.shop.ui.product.adapter.ProductListAdapter
import com.client.shop.ui.product.contract.ProductListPresenter
import com.client.shop.ui.product.contract.ProductListView
import com.domain.entity.Product
import com.domain.entity.SortType
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.ui.base.lce.BaseFragment
import com.ui.base.recycler.OnItemClickListener
import com.ui.base.recycler.divider.GridSpaceDecoration
import kotlinx.android.synthetic.main.fragment_popular.*
import javax.inject.Inject

class PopularFragment :
        BaseFragment<List<Product>, ProductListView, ProductListPresenter>(),
        ProductListView,
        OnItemClickListener {

    @Inject lateinit var productListPresenter: ProductListPresenter
    private val productList = mutableListOf<Product>()
    private val sortType = SortType.RELEVANT
    private lateinit var adapter: ProductListAdapter

    companion object {
        private const val SPAN_COUNT = 2
        private const val MAX_ITEMS = 4
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seeAll.setOnClickListener {
            startActivity(ProductListActivity.getStartIntent(context,
                    getString(R.string.popular), sortType))
        }
        changeSeeAllState()
        setupRecycler()
        loadData(true)
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachPopularComponent(PopularModule()).inject(this)
    }

    override fun getContentView() = R.layout.fragment_popular

    override fun createPresenter() = productListPresenter

    //SETUP

    private fun setupRecycler() {
        val layoutManager = GridLayoutManager(context, SPAN_COUNT)
        val size = resources.getDimensionPixelSize(R.dimen.product_item_size)
        adapter = ProductListAdapter(MATCH_PARENT, size, productList, this)
        GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(GridSpaceDecoration(
                resources.getDimensionPixelSize(R.dimen.recycler_padding), SPAN_COUNT))
    }

    private fun changeSeeAllState() {
        if (productList.size == MAX_ITEMS) {
            seeAll.visibility = View.VISIBLE
        } else {
            seeAll.visibility = View.GONE
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadProductList(sortType, MAX_ITEMS)
    }

    override fun showContent(data: List<Product>) {
        super.showContent(data)

        productList.clear()
        if (data.size >= MAX_ITEMS) {
            productList.addAll(data.subList(0, MAX_ITEMS))
        } else {
            productList.addAll(data)
        }
        adapter.notifyDataSetChanged()
        changeSeeAllState()
    }

    //CALLBACK

    override fun onItemClicked(position: Int) {
        productList.getOrNull(position)?.let {
            startActivity(ProductDetailsActivity.getStartIntent(context, it.id))
        }
    }
}