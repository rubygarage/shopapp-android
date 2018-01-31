package com.client.shop.ui.cart

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.cart.contract.CartWidgetPresenter
import com.client.shop.ui.cart.contract.CartWidgetView
import com.client.shop.ui.cart.di.CartModule
import com.hannesdorfmann.mosby3.mvp.layout.MvpFrameLayout
import kotlinx.android.synthetic.main.widget_cart.view.*
import javax.inject.Inject


class CartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    MvpFrameLayout<CartWidgetView, CartWidgetPresenter>(context, attrs, defStyleAttr),
    CartWidgetView,
    View.OnClickListener {

    @Inject
    lateinit var cartWidgetPresenter: CartWidgetPresenter

    init {
        ShopApplication.appComponent.attachCartComponent(CartModule()).inject(this)
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            layoutParams = LayoutParams(actionBarHeight, actionBarHeight)
        }
        View.inflate(context, R.layout.widget_cart, this)
        image.setOnClickListener(this)
    }

    override fun createPresenter() = cartWidgetPresenter

    override fun changeBadgeCount(count: Int) {
        if (count > 0) {
            badge.visibility = View.VISIBLE
            badge.text = count.toString()
        } else {
            badge.visibility = View.INVISIBLE
        }
    }

    override fun onClick(v: View?) {
        context.startActivity(CartActivity.getStartIntent(context))
    }
}