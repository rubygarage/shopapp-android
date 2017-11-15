package com.shopify.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shopify.api.R
import kotlinx.android.synthetic.main.fragment_payment_method.*

class PaymentMethodFragment :
        Fragment(),
        View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_payment_method, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webPaymentButton.setOnClickListener(this)
        cartPaymentButton.setOnClickListener(this)
        androidPaymentButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val containerActivity = activity
        if (containerActivity is CheckoutActivity) {
            when (v.id) {
                webPaymentButton.id -> containerActivity.startWebPaymentFlow()
                cartPaymentButton.id -> {
                }
                androidPaymentButton.id -> {
                }
            }
        }
    }
}