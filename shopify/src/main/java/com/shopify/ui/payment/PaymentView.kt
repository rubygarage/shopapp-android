package com.shopify.ui.payment

import android.content.Context
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.domain.detector.CardTypeDetector
import com.domain.entity.Address
import com.domain.entity.Card
import com.shopify.api.R
import com.shopify.constant.ANDROID_PAYMENT
import com.shopify.constant.CARD_PAYMENT
import com.shopify.constant.WEB_PAYMENT
import kotlinx.android.synthetic.main.view_payment.view.*

class PaymentView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val LAST_CARD_NUMBERS = 4
        private const val LAST_DATE_NUMBERS = 2
    }

    private val cardTypeDetector = CardTypeDetector()
    private var paymentType: String? = null
    private var cardData: Pair<Card?, String?> = Pair(null, null)
    private var address: Address? = null

    init {
        View.inflate(context, R.layout.view_payment, this)
        setBackgroundColor(Color.WHITE)
    }

    fun getPaymentType() = paymentType

    fun setPaymentType(paymentType: String?) {
        this.paymentType = paymentType
        when (paymentType) {
            WEB_PAYMENT -> {
                setupPaymentTypeText(R.drawable.ic_web, R.string.credit_card)
            }
            CARD_PAYMENT -> {
                setupPaymentTypeText(R.drawable.ic_credit_card, R.string.credit_card)
            }
            ANDROID_PAYMENT -> {
                setupPaymentTypeText(R.drawable.ic_android, R.string.android_payment)
            }
            else -> {
                paymentGroup.visibility = View.GONE
                addPaymentTypeButton.visibility = View.VISIBLE
            }
        }
        changeCardDefaultButtonsVisibility(paymentType)
        changeCardGroupsVisibility(paymentType)
    }

    private fun setupPaymentTypeText(@DrawableRes paymentIcon: Int, @StringRes paymentLabel: Int) {
        paymentTypeText.setCompoundDrawablesWithIntrinsicBounds(paymentIcon, 0, 0, 0)
        paymentTypeText.setText(paymentLabel)
        addPaymentTypeButton.visibility = View.GONE
        paymentGroup.visibility = View.VISIBLE
    }

    private fun changeCardDefaultButtonsVisibility(paymentType: String?) {
        val visibility = if (paymentType == CARD_PAYMENT) View.VISIBLE else View.GONE
        addCardButton.visibility = visibility
        addAddressButton.visibility = visibility
        addressBottomSpace.visibility = visibility
        cardTypeDivider.visibility = visibility
    }

    private fun changeCardGroupsVisibility(paymentType: String?) {
        if (paymentType != CARD_PAYMENT) {
            addressGroup.visibility = View.GONE
            cardGroup.visibility = View.GONE
        }
    }

    fun setCardData(card: Card?, cardToken: String?) {
        cardData = Pair(card, cardToken)
        cardGroup.visibility = if (card != null) {
            val cardType = cardTypeDetector.detect(card.cardNumber)
            if (cardType != null) {
                cardLogo.visibility = if (cardType.logoRes != 0) View.VISIBLE else View.GONE
                cardLogo.setImageResource(cardType.logoRes)
                cardDataText.text = context.getString(
                    R.string.card_type_placeholder,
                    context.getString(cardType.nameRes),
                    card.cardNumber.takeLast(LAST_CARD_NUMBERS))
            } else {
                cardLogo.visibility = View.GONE
                cardDataText.text = context.getString(
                    R.string.card_unknown_placeholder,
                    card.cardNumber.takeLast(LAST_CARD_NUMBERS)
                )
            }
            cardExpiredDate.text = context.getString(R.string.card_exp_placeholder, card.expireMonth, card.expireYear.takeLast(LAST_DATE_NUMBERS))
            cardHolder.text = context.getString(R.string.full_name_pattern, card.firstName, card.lastName)
            addCardButton.visibility = View.GONE
            View.VISIBLE
        } else {
            addCardButton.visibility = View.VISIBLE
            View.GONE
        }
    }

    fun getCardData() = cardData

    fun setAddressData(address: Address?) {
        this.address = address
        addressGroup.visibility = if (address != null) {
            billingAddressData.setAddress(address)
            addAddressButton.visibility = View.GONE
            View.VISIBLE
        } else {
            addAddressButton.visibility = View.VISIBLE
            View.GONE
        }
    }

    fun getAddress() = address

    fun setClickListeners(
        paymentClickListener: OnClickListener,
        cardClickListener: OnClickListener,
        addAddressClickListener: OnClickListener
    ) {
        addPaymentTypeButton.setOnClickListener(paymentClickListener)
        editPaymentButton.setOnClickListener(paymentClickListener)
        addCardButton.setOnClickListener(cardClickListener)
        editCardButton.setOnClickListener(cardClickListener)
        addAddressButton.setOnClickListener(addAddressClickListener)
        editAddressButton.setOnClickListener(addAddressClickListener)
    }
}