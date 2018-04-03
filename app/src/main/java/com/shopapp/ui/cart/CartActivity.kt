package com.shopapp.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.lce.view.LceEmptyView
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.SwipeToDeleteCallback
import com.shopapp.ui.base.recycler.divider.SpaceDecoration
import com.shopapp.ui.cart.adapter.CartAdapter
import com.shopapp.ui.cart.contract.CartPresenter
import com.shopapp.ui.cart.contract.CartView
import com.shopapp.ui.cart.router.CartRouter
import com.shopapp.ui.item.cart.CartItem
import kotlinx.android.synthetic.main.activity_cart.*
import javax.inject.Inject

class CartActivity :
    BaseLceActivity<List<CartProduct>, CartView, CartPresenter>(),
    CartView,
    OnItemClickListener,
    CartItem.ActionListener,
    SwipeToDeleteCallback.OnItemSwipeListener {

    @Inject
    lateinit var cartPresenter: CartPresenter

    @Inject
    lateinit var router: CartRouter

    private val data: MutableList<CartProduct> = mutableListOf()
    private lateinit var adapter: CartAdapter
    private val formatter = NumberFormatter()

    companion object {
        fun getStartIntent(context: Context) = Intent(context, CartActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkoutButton.setOnClickListener { router.showCheckout(this) }
        setupRecyclerView()

        loadData()
        setTitle(getString(R.string.my_cart))
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachCartComponent().inject(this)
    }

    override fun getContentView() = R.layout.activity_cart

    override fun createPresenter() = cartPresenter

    override fun useModalStyle() = true

    //SETUP

    private fun setupRecyclerView() {
        adapter = CartAdapter(data, this)
        adapter.setHasStableIds(true)
        adapter.actionListener = this
        val decoration =
            SpaceDecoration(topSpace = resources.getDimensionPixelSize(R.dimen.cart_item_divider))
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(decoration)
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(this))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun setupEmptyView(emptyView: LceEmptyView) {
        emptyView.customiseEmptyImage(R.drawable.ic_cart_empty)
        emptyView.customiseEmptyMessage(R.string.empty_cart_message)
        emptyView.customiseEmptyButtonText(R.string.start_shopping)
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
        totalPriceView.setData(this.data, formatter)
    }

    //CALLBACK

    override fun onEmptyButtonClicked() {
        router.showHome(this, true)
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out)
    }

    override fun onItemClicked(position: Int) {
        data.getOrNull(position)?.let { router.showProduct(this, it.productVariant) }
    }

    override fun onRemoveButtonClicked(productVariantId: String) {
        presenter.removeProduct(productVariantId)
    }

    override fun onQuantityChanged(productVariantId: String, newQuantity: Int) {
        presenter.changeProductQuantity(productVariantId, newQuantity)
    }

    override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        data.getOrNull(viewHolder?.adapterPosition ?: -1)?.let {
            presenter.removeProduct(it.productVariant.id)
        }
    }
}