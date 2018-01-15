package com.shopify.ui.payment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.domain.detector.CardTypeDetector
import com.domain.entity.Address
import com.domain.entity.Card
import com.shopify.api.R
import com.shopify.constant.*
import kotlinx.android.synthetic.main.view_payment.view.*

class PaymentView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val LAST_CARD_NUMBERS = 4
        private const val LAST_DATE_NUMBERS = 2
    }

    private val cardTypeDetector = CardTypeDetector()

    init {
        View.inflate(context, R.layout.view_payment, this)
        setBackgroundColor(Color.WHITE)
    }

    fun setData(data: Intent?) {
        if (data != null) {
            addPaymentTypeButton.visibility = GONE
            editButton.visibility = View.VISIBLE
            setPaymentType(data)
            setCardData(data)
            setAddressData(data)
        } else {
            addPaymentTypeButton.visibility = View.VISIBLE
            paymentType.visibility = View.GONE
            editButton.visibility = View.GONE
        }
    }

    private fun setPaymentType(data: Intent) {
        paymentType.visibility = View.VISIBLE
        @PaymentType val type: String? = data.getStringExtra(Extra.PAYMENT_TYPE)
        when (type) {
            WEB_PAYMENT -> {
                paymentType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_web, 0, 0, 0)
                paymentType.setText(R.string.credit_card)
            }
            CARD_PAYMENT -> {
                paymentType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_credit_card, 0, 0, 0)
                paymentType.setText(R.string.credit_card)
            }
            ANDROID_PAYMENT -> {
                paymentType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_android, 0, 0, 0)
                paymentType.setText(R.string.android_payment)
            }
            else -> {
                paymentType.visibility = View.GONE
            }
        }
    }

    private fun setCardData(data: Intent) {
        val card = data.getParcelableExtra<Card>(Extra.CARD)
        cardGroup.visibility = if (card != null) {
            val cardType = cardTypeDetector.detect(card.cardNumber, context)
            if (cardType != null) {
                cardData.text = context.getString(R.string.card_type_placeholder, cardType,
                        card.cardNumber.takeLast(LAST_CARD_NUMBERS))
            } else {
                cardData.text = context.getString(R.string.card_unknown_placeholder,
                        card.cardNumber.takeLast(LAST_CARD_NUMBERS))
            }
            cardExpiredDate.text = context.getString(R.string.card_exp_placeholder, card.expireMonth, card.expireYear.takeLast(LAST_DATE_NUMBERS))
            cardHolder.text = context.getString(R.string.full_name_pattern, card.firstName, card.lastName)
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun setAddressData(data: Intent) {
        val address = data.getParcelableExtra<Address>(Extra.ADDRESS)
        addressGroup.visibility = if (address != null) {
            billingAddressData.setAddress(address)
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    fun setClickListeners(addAddressClickListener: OnClickListener) {
        editButton.setOnClickListener(addAddressClickListener)
        addPaymentTypeButton.setOnClickListener(addAddressClickListener)
    }
}