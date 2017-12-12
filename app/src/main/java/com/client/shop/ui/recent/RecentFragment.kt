package com.client.shop.ui.recent

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.const.Constant.DEFAULT_PER_PAGE_COUNT
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.base.ui.recycler.divider.LeftSpaceDecoration
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.recent.adapter.RecentAdapter
import com.client.shop.ui.recent.contract.RecentPresenter
import com.client.shop.ui.recent.contract.RecentView
import com.client.shop.ui.recent.di.RecentModule
import com.domain.entity.Product
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.ui.base.lce.BaseFragment
import kotlinx.android.synthetic.main.fragment_recent.*
import javax.inject.Inject

class RecentFragment :
        BaseFragment<List<Product>, RecentView, RecentPresenter>(),
        RecentView,
        OnItemClickListener {

    @Inject lateinit var recentPresenter: RecentPresenter

    private val productList = mutableListOf<Product>()
    private lateinit var adapter: RecentAdapter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seeAll.setOnClickListener { startActivity(RecentActivity.getStartIntent(context)) }
        changeSeeAllState()
        setupRecycler()
        loadData()
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachRecentComponent(RecentModule()).inject(this)
    }

    override fun getContentView() = R.layout.fragment_recent

    override fun createPresenter() = recentPresenter

    //SETUP

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = RecentAdapter(productList, this)
        GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView)
        val decoration = LeftSpaceDecoration(resources.getDimensionPixelSize(R.dimen.content_space))
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
        presenter.loadProductList(DEFAULT_PER_PAGE_COUNT)
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