package com.client.shop.ui.recent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.const.Constant.DEFAULT_PER_PAGE_COUNT
import com.client.shop.ui.base.ui.pagination.PaginationActivity
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.recent.adapter.RecentAdapter
import com.client.shop.ui.recent.contract.RecentPresenter
import com.client.shop.ui.recent.contract.RecentView
import com.client.shop.ui.recent.di.RecentModule
import com.domain.entity.Product
import kotlinx.android.synthetic.main.activity_pagination.*
import javax.inject.Inject

class RecentActivity :
        PaginationActivity<Product, RecentView, RecentPresenter>(),
        RecentView {

    @Inject lateinit var recentPresenter: RecentPresenter

    companion object {
        fun getStartIntent(context: Context) = Intent(context, RecentActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.latest_arrivals))
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachRecentComponent(RecentModule()).inject(this)
    }

    override fun createPresenter() = recentPresenter

    override fun isGrid() = true

    //SETUP

    override fun setupAdapter() = RecentAdapter(dataList, this)

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        recyclerView.setBackgroundResource(R.color.white)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadProductList(DEFAULT_PER_PAGE_COUNT, paginationValue)
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