package com.shopapp.ui.checkout.view

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.shopapp.R
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.divider.SpaceDecoration
import com.shopapp.ui.product.ProductDetailsActivity
import com.shopapp.ui.product.adapter.ProductVariantAdapter
import kotlinx.android.synthetic.main.view_checkout_my_cart.view.*

class CheckoutMyCartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr),
    OnItemClickListener {

    private val cartProductList = mutableListOf<ProductVariant>()

    init {
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