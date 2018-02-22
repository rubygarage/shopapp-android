package com.shopapp.ui.checkout.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.CompoundButton
import com.shopapp.R
import com.shopapp.ui.const.Extra
import com.shopapp.ui.const.PaymentType
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    companion object {

        private const val PAYMENT_TYPE = "payment_type"

        fun getStartIntent(context: Context, paymentType: PaymentType?): Intent {
            val intent = Intent(context, PaymentActivity::class.java)
            intent.putExtra(PAYMENT_TYPE, paymentType)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        toolbar.setTitle(getString(R.string.payment_type))
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
            it.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        }

        val paymentType: PaymentType? = intent.getSerializableExtra(PAYMENT_TYPE) as? PaymentType
        when (paymentType) {
            PaymentType.CARD_PAYMENT -> cardPayRadioButton.isChecked = true
            PaymentType.ANDROID_PAYMENT -> androidPayRadioButton.isChecked = true
            PaymentType.WEB_PAYMENT -> webPayRadioButton.isChecked = true
        }

        cardPayRadioButton.setOnCheckedChangeListener(this)
        androidPayRadioButton.setOnCheckedChangeListener(this)
        webPayRadioButton.setOnCheckedChangeListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            false
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val paymentType: PaymentType? = when (buttonView) {
            cardPayRadioButton -> PaymentType.CARD_PAYMENT
            androidPayRadioButton -> PaymentType.ANDROID_PAYMENT
            webPayRadioButton -> PaymentType.WEB_PAYMENT
            else -> null
        }
        val resultData = Intent()
        resultData.putExtra(Extra.PAYMENT_TYPE, paymentType)
        setResult(Activity.RESULT_OK, resultData)
        finish()
    }
}