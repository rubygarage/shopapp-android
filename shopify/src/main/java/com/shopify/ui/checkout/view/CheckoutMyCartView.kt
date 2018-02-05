package com.shopify.ui.checkout.view

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import com.domain.entity.CartProduct
import com.domain.entity.ProductVariant
import com.domain.router.AppRouter
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import com.shopify.ui.checkout.di.CheckoutModule
import com.ui.base.recycler.OnItemClickListener
import com.ui.base.recycler.adapter.ProductVariantAdapter
import com.ui.base.recycler.divider.SpaceDecoration
import kotlinx.android.synthetic.main.view_checkout_my_cart.view.*
import javax.inject.Inject

class CheckoutMyCartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr),
    OnItemClickListener {

    @Inject
    lateinit var router: AppRouter
    private val cartProductList = mutableListOf<ProductVariant>()

    init {
        ShopifyWrapper.component.attachCheckoutComponent(CheckoutModule()).inject(this)
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
            router.openProductDetailsScreen(context, it)
        }
    }
}