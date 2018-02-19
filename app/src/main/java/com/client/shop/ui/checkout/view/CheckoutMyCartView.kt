package com.client.shop.ui.checkout.view

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.product.adapter.ProductVariantAdapter
import com.client.shop.ui.product.ProductDetailsActivity
import com.client.shop.gateway.entity.CartProduct
import com.client.shop.gateway.entity.ProductVariant
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.client.shop.ui.base.recycler.OnItemClickListener
import com.client.shop.ui.base.recycler.divider.SpaceDecoration
import kotlinx.android.synthetic.main.view_checkout_my_cart.view.*

class CheckoutMyCartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr),
    OnItemClickListener {

    private val cartProductList = mutableListOf<ProductVariant>()

    init {
        ShopApplication.appComponent.attachCheckoutComponent().inject(this)
        View.inflate(context, R.layout.view_checkout_my_cart, this)
        orientation = VERTICAL
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = ProductVariantAdapter(cartProductList, this)
        GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView)
        val decoration =
            SpaceDecoration(leftSpace = resources.getDimensionPixelSize(R.dimen.product_variant_item_divider), skipFirst = true)
        recyclerView.addItemDecoration(decoration)
    }

    fun setData(cartProductList: List<CartProduct>) {
        this.cartProductList.clear()
        this.cartProductList.addAll(cartProductList.map { it.productVariant })
        recyclerView.adapter.notifyDataSetChanged()
    }

    override fun onItemClicked(position: Int) {
        cartProductList.getOrNull(position)?.let {
            context.startActivity(ProductDetailsActivity.getStartIntent(context, it))
        }
    }
}