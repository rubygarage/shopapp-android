package com.client.shop.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.base.ui.recycler.SwipeToDeleteCallback
import com.client.shop.ui.base.ui.recycler.divider.SpaceDecoration
import com.client.shop.ui.cart.adapter.CartAdapter
import com.client.shop.ui.cart.contract.CartPresenter
import com.client.shop.ui.cart.contract.CartView
import com.client.shop.ui.cart.di.CartModule
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.item.cart.CartItem
import com.client.shop.ui.product.ProductListActivity
import com.domain.entity.CartProduct
import com.domain.entity.SortType
import com.domain.router.Router
import com.ui.base.lce.BaseActivity
import com.ui.base.lce.view.LceEmptyView
import kotlinx.android.synthetic.main.activity_cart.*
import javax.inject.Inject

class CartActivity :
        BaseActivity<List<CartProduct>, CartView, CartPresenter>(),
        CartView,
        OnItemClickListener,
        CartItem.ActionListener {

    @Inject lateinit var cartPresenter: CartPresenter
    @Inject lateinit var router: Router
    private val data: MutableList<CartProduct> = mutableListOf()
    private lateinit var adapter: CartAdapter

    companion object {
        fun getStartIntent(context: Context) = Intent(context, CartActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkoutButton.setOnClickListener { router.startCheckoutFlow(this) }
        setupRecyclerView()
        setupEmptyView()

        loadData()
        setTitle(getString(R.string.my_cart))
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachCartComponent(CartModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_cart

    override fun createPresenter() = cartPresenter

    override fun useModalStyle() = true

    //SETUP

    private fun setupRecyclerView() {
        adapter = CartAdapter(data, this)
        adapter.setHasStableIds(true)
        adapter.actionListener = this
        val decoration = SpaceDecoration(topSpace = resources.getDimensionPixelSize(R.dimen.cart_item_divider))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(decoration)
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                data.getOrNull(viewHolder.adapterPosition)?.let {
                    presenter.removeProduct(it.productVariant.id)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun setupEmptyView() {
        val emptyView = findViewById<LceEmptyView>(R.id.emptyView)
        emptyView.customiseEmptyImage(R.drawable.ic_cart_empty)
        emptyView.customiseEmptyMessage(R.string.empty_cart_message)
        emptyView.customiseEmptyButtonText(R.string.empty_cart_button)
        emptyView.customiseEmptyButtonVisibility(true)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadCartItems()
    }

    override fun showContent(data: List<CartProduct>) {
        super.showContent(data)
        this.data.clear()
        this.data.addAll(data)
        adapter.notifyDataSetChanged()
        totalPriceView.setData(this.data)
    }

    //CALLBACK

    override fun emptyButtonClicked() {
        startActivity(ProductListActivity.getStartIntent(this, getString(R.string.latest_arrivals), SortType.RECENT))
    }

    override fun onItemClicked(position: Int) {
        data.getOrNull(position)?.let {
            startActivity(DetailsActivity.getStartIntent(this, it.productId))
        }
    }

    override fun onRemoveButtonClicked(productVariantId: String) {
        presenter.removeProduct(productVariantId)
    }

    override fun onQuantityChanged(productVariantId: String, newQuantity: Int) {
        presenter.changeProductQuantity(productVariantId, newQuantity)
    }
}