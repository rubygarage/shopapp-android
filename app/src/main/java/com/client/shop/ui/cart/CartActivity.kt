package com.client.shop.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.ui.lce.BaseActivity
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.cart.adapter.CartAdapter
import com.client.shop.ui.cart.contract.CartPresenter
import com.client.shop.ui.cart.contract.CartView
import com.client.shop.ui.cart.di.CartModule
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.item.cart.CartItem
import com.client.shop.ui.recent.RecentActivity
import com.domain.entity.CartProduct
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import javax.inject.Inject

class CartActivity :
        BaseActivity<List<CartProduct>, CartView, CartPresenter>(),
        CartView,
        OnItemClickListener<CartProduct>,
        CartItem.ActionListener {

    @Inject lateinit var cartPresenter: CartPresenter
    private val data: MutableList<CartProduct> = mutableListOf()
    private lateinit var adapter: CartAdapter

    companion object {
        fun getStartIntent(context: Context) = Intent(context, CartActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupRecyclerView()
        setupEmptyView()

        loadData(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.cart)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //INIT

    override fun inject(component: AppComponent) {
        component.attachCartComponent(CartModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_cart

    override fun createPresenter() = cartPresenter

    //SETUP

    private fun setupRecyclerView() {
        adapter = CartAdapter(data, this)
        adapter.setHasStableIds(true)
        adapter.actionListener = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupEmptyView() {
        val emptyView = lceLayout.emptyView
        emptyView.customiseEmptyImage(R.drawable.ic_empty_shopping_cart)
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
    }

    //CALLBACK

    override fun emptyButtonClicked() {
        startActivity(RecentActivity.getStartIntent(this))
    }

    override fun onItemClicked(data: CartProduct, position: Int) {
        startActivity(DetailsActivity.getStartIntent(this, data.productId))
    }

    override fun onRemoveButtonClicked(productVariantId: String) {
        presenter.removeProduct(productVariantId)
    }

    override fun onQuantityChanged(productVariantId: String, newQuantity: Int) {
        presenter.changeProductQuantity(productVariantId, newQuantity)
    }
}