package com.shopapp.ui.order.success

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ui.order.success.router.OrderSuccessRouter
import kotlinx.android.synthetic.main.activity_order_success.*
import javax.inject.Inject

class OrderSuccessActivity : AppCompatActivity() {

    companion object {

        const val ORDER_ID = "order_id"
        const val ORDER_NUMBER = "order_number"

        fun getStartIntent(context: Context, orderId: String, orderNumber: Int): Intent {
            val intent = Intent(context, OrderSuccessActivity::class.java)
            intent.putExtra(ORDER_ID, orderId)
            intent.putExtra(ORDER_NUMBER, orderNumber)
            return intent
        }
    }

    @Inject
    lateinit var router: OrderSuccessRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        ShopApplication.appComponent.attachOrderComponent().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_success)
        toolbar.setTitle(getString(R.string.shop))

        val orderId = intent.getStringExtra(ORDER_ID)
        val orderNumber = intent.getIntExtra(ORDER_NUMBER, 0)

        orderNumberValue.text = orderNumber.toString()
        viewOrderButton.setOnClickListener {
            router.showOrder(this, orderId)
            finish()
        }
        continueShipping.setOnClickListener {
            onBackPressed()
        }
    }
}