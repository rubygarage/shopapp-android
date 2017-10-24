package com.client.shop.ui.recent

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.ui.BaseMvpFragment
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.recent.adapter.RecentAdapter
import com.client.shop.ui.recent.contract.RecentPresenter
import com.client.shop.ui.recent.contract.RecentView
import com.client.shop.ui.recent.di.RecentModule
import com.domain.entity.Product
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.android.synthetic.main.fragment_recent.*
import javax.inject.Inject

class RecentFragment : BaseMvpFragment<RecentView, RecentPresenter, RecentViewState>(), RecentView,
        OnItemClickListener<Product> {

    @Inject lateinit var recentPresenter: RecentPresenter

    private val productList = mutableListOf<Product>()
    private lateinit var adapter: RecentAdapter

    companion object {
        private const val RECENT_ITEMS_COUNT = 10
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
    }

    private fun setupRecycler() {

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = RecentAdapter(productList, this,
                View.OnClickListener { startActivity(RecentActivity.getStartIntent(context)) })
        GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
    }

    override fun inject(component: AppComponent) {
        component.attachRecentComponent(RecentModule()).inject(this)
    }

    override fun createPresenter() = recentPresenter

    override fun createViewState() = RecentViewState()

    override fun onNewViewStateInstance() {
        presenter.loadProductList(RECENT_ITEMS_COUNT)
    }

    override fun onItemClicked(data: Product, position: Int) {
        startActivity(DetailsActivity.getStartIntent(context, data))
    }

    /*VIEW IMPLEMENTATION*/

    override fun productListLoaded(productList: List<Product>) {
        viewState.setData(productList)
        this.productList.clear()
        this.productList.addAll(productList)
        adapter.withFooter = this.productList.size == RECENT_ITEMS_COUNT
        adapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        recentProgress.show()
    }

    override fun hideProgress() {
        recentProgress.hide()
    }
}