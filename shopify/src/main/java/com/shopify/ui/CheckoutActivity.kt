package com.shopify.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.repository.CartRepository
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import kotlinx.android.synthetic.main.activity_checkout.*
import javax.inject.Inject

class CheckoutActivity : AppCompatActivity() {

    @Inject lateinit var repo: CartRepository

    companion object {
        fun getStartIntent(context: Context) = Intent(context, CheckoutActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ShopifyWrapper.component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, PaymentMethodFragment())
                .commit()

        Log.d("TEST", repo.toString())
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun startWebPaymentFlow() {

    }
}