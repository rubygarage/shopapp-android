package com.shopify.ui.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.view.Gravity
import android.view.View
import com.domain.entity.CartProduct
import com.domain.router.AppRouter
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import com.shopify.entity.Checkout
import com.shopify.ui.address.AddressActivity
import com.shopify.ui.address.AddressListActivity
import com.shopify.ui.checkout.contract.CheckoutPresenter
import com.shopify.ui.checkout.contract.CheckoutView
import com.shopify.ui.checkout.di.CheckoutModule
import com.shopify.ui.payment.PaymentActivity
import com.ui.base.lce.BaseActivity
import com.ui.base.recycler.OnItemClickListener
import com.ui.base.recycler.adapter.ProductVariantAdapter
import com.ui.base.recycler.divider.SpaceDecoration
import com.ui.const.RequestCode
import kotlinx.android.synthetic.main.activity_checkout.*
import java.math.BigDecimal
import javax.inject.Inject

class CheckoutActivity :
    BaseActivity<Checkout, CheckoutView, CheckoutPresenter>(),
    CheckoutView,
    OnItemClickListener {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, CheckoutActivity::class.java)
    }

    @Inject
    lateinit var checkoutPresenter: CheckoutPresenter
    @Inject
    lateinit var router: AppRouter
    private var checkout: Checkout? = null
    private val cartProductList = mutableListOf<CartProduct>()

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.checkout))

        setupCartRecycler()
        setupListeners()
        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.ADD_ADDRESS || requestCode == RequestCode.EDIT_ADDRESS) {
            if (resultCode == Activity.RESULT_OK) {
                loadData()
            }
        } else if (requestCode == RequestCode.PAYMENT && resultCode == Activity.RESULT_OK) {
            paymentView.setData(data)
        }
    }

    //INIT

    override fun inject() {
        ShopifyWrapper.component
            .attachCheckoutComponent(CheckoutModule())
            .inject(this)
    }

    override fun getContentView() = R.layout.activity_checkout

    override fun createPresenter() = checkoutPresenter

    override fun useModalStyle() = true

    //SETUP

    private fun setupCartRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
        recyclerView.adapter =
                ProductVariantAdapter(cartProductList.map { it.productVariant }, this)
        GravitySnapHelper(Gravity.START).attachToRecyclerView(recyclerView)
        val decoration =
            SpaceDecoration(leftSpace = resources.getDimensionPixelSize(R.dimen.product_variant_item_divider))
        recyclerView.addItemDecoration(decoration)
    }

    private fun setupListeners() {
        seeAll.setOnClickListener { router.openCartScreen(this) }
        shippingAddressView.setClickListeners(
            editClickListener = View.OnClickListener {
                checkout?.let {
                    startActivityForResult(
                        AddressListActivity.getStartIntent(this, it.checkoutId, it.address),
                        RequestCode.EDIT_ADDRESS
                    )
                }
            },
            addAddressClickListener = View.OnClickListener {
                checkout?.let {
                    startActivityForResult(
                        AddressActivity.getStartIntent(this, it.checkoutId),
                        RequestCode.ADD_ADDRESS
                    )
                }
            }
        )
        paymentView.setClickListeners(
            addAddressClickListener = View.OnClickListener {
                startActivityForResult(PaymentActivity.getStartIntent(this), RequestCode.PAYMENT)
            }
        )
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        val checkout = this.checkout
        if (checkout != null) {
            presenter.getCheckout(checkout.checkoutId)
        } else {
            presenter.getCartProductList()
        }
    }

    override fun showContent(data: Checkout) {
        super.showContent(data)
        checkout = data
        shippingAddressView.setAddress(data.address)
        priceView.setData(
            data.subtotalPrice, BigDecimal.ZERO,
            data.shippingRate?.price ?: BigDecimal.ZERO, data.totalPrice, data.currency
        )
    }

    override fun cartProductListReceived(cartProductList: List<CartProduct>) {
        this.cartProductList.clear()
        this.cartProductList.addAll(cartProductList)
        recyclerView.adapter.notifyDataSetChanged()
    }

    //CALLBACK

    override fun onItemClicked(position: Int) {
        cartProductList.getOrNull(position)?.let {
            router.openProductDetailsScreen(this, it.productVariant.productId)
        }
    }
}