package com.client.shop.ui.recent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.ui.PaginationActivity
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.recent.adapter.RecentAdapter
import com.client.shop.ui.recent.contract.RecentPresenter
import com.client.shop.ui.recent.contract.RecentView
import com.client.shop.ui.recent.di.RecentModule
import com.domain.entity.Product
import javax.inject.Inject

class RecentActivity : PaginationActivity<Product, RecentView, RecentPresenter, RecentViewState>(),
        RecentView, SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var recentPresenter: RecentPresenter

    companion object {
        private const val RECENT_ITEMS_COUNT = 10
        fun getStartIntent(context: Context) = Intent(context, RecentActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.last_arrivals)
    }

    override fun inject(component: AppComponent) {
        component.attachRecentComponent(RecentModule()).inject(this)
    }

    override fun createPresenter() = recentPresenter

    override fun getContentView() = R.layout.activity_pagination

    override fun createViewState() = RecentViewState()

    override fun onNewViewStateInstance() {
        fetchData()
    }

    override fun isGrid() = true

    override fun initAdapter() = RecentAdapter(dataList, this, View.OnClickListener { })

    override fun onItemClicked(data: Product, position: Int) {
        startActivity(DetailsActivity.getStartIntent(this, data))
    }

    override fun fetchData() {
        presenter.loadProductList(RECENT_ITEMS_COUNT, paginationValue)
    }

    override fun productListLoaded(productList: List<Product>) {
        if (productList.isNotEmpty()) {
            paginationValue = productList.last().paginationValue
            this.dataList.addAll(productList)
            adapter.notifyDataSetChanged()
        }
        viewState.setData(this.dataList)
    }
}