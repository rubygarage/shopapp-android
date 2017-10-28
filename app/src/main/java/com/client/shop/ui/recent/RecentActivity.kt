package com.client.shop.ui.recent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.client.shop.R
import com.client.shop.const.Constant.DEFAULT_PER_PAGE_COUNT
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.ui.pagination.PaginationActivity
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.recent.adapter.RecentAdapter
import com.client.shop.ui.recent.contract.RecentPresenter
import com.client.shop.ui.recent.contract.RecentView
import com.client.shop.ui.recent.di.RecentModule
import com.domain.entity.Product
import javax.inject.Inject

class RecentActivity :
        PaginationActivity<Product, RecentView, RecentPresenter>(),
        RecentView {

    @Inject lateinit var recentPresenter: RecentPresenter

    companion object {
        fun getStartIntent(context: Context) = Intent(context, RecentActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.last_arrivals)
        loadData(false)
    }

    override fun inject(component: AppComponent) {
        component.attachRecentComponent(RecentModule()).inject(this)
    }

    override fun createPresenter() = recentPresenter

    override fun getContentView() = R.layout.activity_pagination

    override fun isGrid() = true

    override fun setupAdapter() = RecentAdapter(dataList, this, View.OnClickListener { })

    override fun onItemClicked(data: Product, position: Int) {
        startActivity(DetailsActivity.getStartIntent(this, data.id))
    }

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
}