package com.shopify.ui.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.shopify.api.R
import com.shopify.ui.address.CheckoutAddressListActivity
import com.shopify.ui.payment.card.CardActivity
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, PaymentActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        toolbar.setTitle(getString(R.string.payment_type))
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(com.ui.R.drawable.ic_arrow_left)
        }

        creditCardPay.setOnClickListener {
            startActivity(CardActivity.getStartIntent(this))
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item?.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}