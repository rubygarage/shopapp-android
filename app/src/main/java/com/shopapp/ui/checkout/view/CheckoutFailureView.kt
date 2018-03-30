package com.shopapp.ui.checkout.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.shopapp.R
import kotlinx.android.synthetic.main.view_checkout_failure.view.*

class CheckoutFailureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_checkout_failure, this)
        setBackgroundResource(R.color.white)
    }

    fun setListeners(tryAgainClickListener: OnClickListener, backToShopClickListener: OnClickListener) {
        tryAgainCheckoutButton.setOnClickListener(tryAgainClickListener)
        backToShop.setOnClickListener(backToShopClickListener)
    }
}