package com.shopapp.ui.product

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.base.lce.BaseLceFragment
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.divider.SpaceDecoration
import com.shopapp.ui.const.Constant
import com.shopapp.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import com.shopapp.ui.product.adapter.ProductListAdapter
import com.shopapp.ui.product.contract.ProductListPresenter
import com.shopapp.ui.product.contract.ProductListView
import com.shopapp.ui.product.router.ProductRouter
import kotlinx.android.synthetic.main.fragment_product_shortcut.*
import javax.inject.Inject

class ProductShortcutFragment :
    BaseLceFragment<List<Product>, ProductListView, ProductListPresenter>(),
    ProductListView,
    OnItemClickListener {

    companion object {

        const val SORT_TYPE = "sort_type"
        const val KEYWORD = "keyword"
        const val EXCLUDE_KEYWORD = "exclude_keyword"
        const val IS_HORIZONTAL_MODE = "is_horizontal_mode"

        fun newInstance(
            sortType: SortType,
            keyword: String? = null,
            excludeKeyword: String? = null,
            isHorizontalMode: Boolean = true
        ): ProductShortcutFragment {
            val fragment = ProductShortcutFragment()
            val args = Bundle()
            args.putSerializable(SORT_TYPE, sortType)
            args.putString(KEYWORD, keyword)
            args.putString(EXCLUDE_KEYWORD, excludeKeyword)
            args.putBoolean(IS_HORIZONTAL_MODE, isHorizontalMode)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var productListPresenter: ProductListPresenter

    @Inject
    lateinit var router: ProductRouter

    private val productList = mutableListOf<Product>()
    private var sortType = SortType.RECENT
    private var keyword: String? = null
    private var excludeKeyword: String? = null
    private var isHorizontalMode: Boolean = true
    private lateinit var adapter: ProductListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            sortType = it.getSerializable(SORT_TYPE) as SortType
            keyword = it.getString(KEYWORD)
            excludeKeyword = it.getString(EXCLUDE_KEYWORD)
            isHorizontalMode = it.getBoolean(IS_HORIZONTAL_MODE, true)
        }

        val title = when (sortType) {
            SortType.RECENT -> getString(R.string.latest_arrivals)
            SortType.TYPE -> getString(R.string.related_items)
            else -> ""
        }
        titleText.text = title
        seeAll.setOnClickListener { router.showProductList(context, title, sortType, keyword, excludeKeyword) }
        changeSeeAllState()
        setupRecycler()
        loadData(true)
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachProductComponent().inject(this)
    }

    override fun getContentView() = R.layout.fragment_product_shortcut

    override fun createPresenter() = productListPresenter

    //SETUP

    private fun setupRecycler() {
        val layoutManager: RecyclerView.LayoutManager
        val height: Int
        val width: Int
        if (isHorizontalMode) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.setHasFixedSize(true)
            GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView)
            height = resources.getDimensionPixelSize(R.dimen.product_horizontal_item_size)
            width = height
        } else {
            layoutManager = GridLayoutManager(context, Constant.SPAN_COUNT)
            recyclerView.isNestedScrollingEnabled = false
            height = resources.getDimensionPixelSize(R.dimen.product_grid_item_size)
            width = MATCH_PARENT
        }
        adapter = ProductListAdapter(width, height, productList, this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val decoration = SpaceDecoration(leftSpace = resources.getDimensionPixelSize(R.dimen.content_space))
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
        fragmentVisibilityListener?.changeVisibility(data.isNotEmpty())
        if (data.isNotEmpty()) {
            productList.clear()
            productList.addAll(data)
            adapter.notifyDataSetChanged()
            changeSeeAllState()
        }
    }

    //CALLBACK

    override fun onItemClicked(position: Int) {
        productList.getOrNull(position)?.let { router.showProduct(context, it.id) }
    }
}