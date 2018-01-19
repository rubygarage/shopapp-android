package com.client.shop.ui.product

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.product.adapter.ProductListAdapter
import com.client.shop.ui.product.contract.ProductListPresenter
import com.client.shop.ui.product.contract.ProductListView
import com.client.shop.ui.product.di.ProductHorizontalModule
import com.domain.entity.Product
import com.domain.entity.SortType
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.ui.base.lce.BaseFragment
import com.ui.base.recycler.OnItemClickListener
import com.ui.base.recycler.divider.SpaceDecoration
import com.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import kotlinx.android.synthetic.main.fragment_recent.*
import javax.inject.Inject

class ProductHorizontalFragment :
    BaseFragment<List<Product>, ProductListView, ProductListPresenter>(),
    ProductListView,
    OnItemClickListener {

    companion object {

        private const val SORT_TYPE = "sort_type"
        private const val KEY_PHRASE = "key_phrase"

        fun newInstance(sortType: SortType, keyPhrase: String? = null): ProductHorizontalFragment {
            val fragment = ProductHorizontalFragment()
            val args = Bundle()
            args.putSerializable(SORT_TYPE, sortType)
            args.putString(KEY_PHRASE, keyPhrase)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var productListPresenter: ProductListPresenter
    private val productList = mutableListOf<Product>()
    private var sortType = SortType.RECENT
    private var keyPhrase: String? = null
    private lateinit var adapter: ProductListAdapter

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortType = arguments.getSerializable(SORT_TYPE) as SortType
        keyPhrase = arguments.getString(KEY_PHRASE)
        val title = when (sortType) {
            SortType.RECENT -> getString(R.string.latest_arrivals)
            SortType.TYPE -> getString(R.string.related_items)
            else -> ""
        }
        titleText.text = title
        seeAll.setOnClickListener {
            startActivity(ProductListActivity.getStartIntent(context, title, sortType, keyPhrase))
        }
        changeSeeAllState()
        setupRecycler()
        loadData(true)
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachRecentComponent(ProductHorizontalModule()).inject(this)
    }

    override fun getContentView() = R.layout.fragment_recent

    override fun createPresenter() = productListPresenter

    //SETUP

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val size = resources.getDimensionPixelSize(R.dimen.product_item_size)
        adapter = ProductListAdapter(size, size, productList, this)
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
        presenter.loadProductList(sortType, DEFAULT_PER_PAGE_COUNT, null, keyPhrase)
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
            startActivity(ProductDetailsActivity.getStartIntent(context, it.id))
        }
    }
}