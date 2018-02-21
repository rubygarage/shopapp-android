package com.client.shop.ui.product

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.gateway.entity.Product
import com.client.shop.gateway.entity.SortType
import com.client.shop.ui.base.lce.BaseLceFragment
import com.client.shop.ui.base.recycler.OnItemClickListener
import com.client.shop.ui.base.recycler.divider.SpaceDecoration
import com.client.shop.ui.base.ui.FragmentVisibilityListener
import com.client.shop.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import com.client.shop.ui.product.adapter.ProductListAdapter
import com.client.shop.ui.product.contract.ProductListPresenter
import com.client.shop.ui.product.contract.ProductListView
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.android.synthetic.main.fragment_recent.*
import javax.inject.Inject

class ProductHorizontalFragment :
    BaseLceFragment<List<Product>, ProductListView, ProductListPresenter>(),
    ProductListView,
    OnItemClickListener {

    companion object {

        private const val SORT_TYPE = "sort_type"
        private const val KEYWORD = "keyword"
        private const val EXCLUDE_KEYWORD = "exclude_keyword"

        fun newInstance(
            sortType: SortType,
            keyword: String? = null,
            excludeKeyword: String? = null
        ): ProductHorizontalFragment {
            val fragment = ProductHorizontalFragment()
            val args = Bundle()
            args.putSerializable(SORT_TYPE, sortType)
            args.putString(KEYWORD, keyword)
            args.putString(EXCLUDE_KEYWORD, excludeKeyword)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var productListPresenter: ProductListPresenter
    private val productList = mutableListOf<Product>()
    private var sortType = SortType.RECENT
    private var keyword: String? = null
    private var excludeKeyword: String? = null
    private lateinit var adapter: ProductListAdapter
    var visibilityListener: FragmentVisibilityListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortType = arguments?.getSerializable(SORT_TYPE) as SortType
        keyword = arguments?.getString(KEYWORD)
        excludeKeyword = arguments?.getString(EXCLUDE_KEYWORD)
        val title = when (sortType) {
            SortType.RECENT -> getString(R.string.latest_arrivals)
            SortType.TYPE -> getString(R.string.related_items)
            else -> ""
        }
        titleText.text = title
        seeAll.setOnClickListener {
            context?.let {
                startActivity(ProductListActivity.getStartIntent(it, title, sortType, keyword, excludeKeyword))
            }
        }
        changeSeeAllState()
        setupRecycler()
        loadData(true)
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachRecentComponent().inject(this)
    }

    override fun getContentView() = R.layout.fragment_recent

    override fun createPresenter() = productListPresenter

    //SETUP

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val size = resources.getDimensionPixelSize(R.dimen.product_horizontal_item_size)
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
        presenter.loadProductList(sortType, DEFAULT_PER_PAGE_COUNT, null, keyword, excludeKeyword)
    }

    override fun showContent(data: List<Product>) {
        super.showContent(data)
        visibilityListener?.changeVisibility(data.isNotEmpty())
        if (data.isNotEmpty()) {
            productList.clear()
            productList.addAll(data)
            adapter.notifyDataSetChanged()
            changeSeeAllState()
        }
    }

    //CALLBACK

    override fun onItemClicked(position: Int) {
        productList.getOrNull(position)?.let {
            val productId = it.id
            context?.let {
                startActivity(ProductDetailsActivity.getStartIntent(it, productId))
            }
        }
    }
}