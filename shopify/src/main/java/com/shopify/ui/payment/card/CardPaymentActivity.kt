package com.shopify.ui.payment.card

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cooltechworks.creditcarddesign.CardEditActivity
import com.cooltechworks.creditcarddesign.CreditCardUtils
import com.domain.entity.Address
import com.domain.entity.Card
import com.domain.validator.CardValidator
import com.shopify.ShopifyWrapper
import com.shopify.api.R
import com.shopify.entity.Checkout
import com.shopify.ui.payment.card.contract.CardPaymentPresenter
import com.shopify.ui.payment.card.contract.CardPaymentView
import com.shopify.ui.payment.card.di.CardPaymentModule
import com.ui.base.lce.BaseActivity
import kotlinx.android.synthetic.main.activity_card_payment.*
import javax.inject.Inject


class CardPaymentActivity : BaseActivity<Boolean, CardPaymentView, CardPaymentPresenter>(), CardPaymentView {

    companion object {
        private const val CHECKOUT = "checkout"
        private const val ADDRESS = "address"
        private const val CARD_EDIT = 1000

        fun getStartIntent(context: Context, checkout: Checkout, address: Address): Intent {
            val intent = Intent(context, CardPaymentActivity::class.java)
            intent.putExtra(CHECKOUT, checkout)
            intent.putExtra(ADDRESS, address)
            return intent
        }
    }

    @Inject lateinit var cardPaymentPresenter: CardPaymentPresenter
    private lateinit var checkout: Checkout
    private lateinit var address: Address
    private val cardManager = CardValidator()
    private var card: Card? = null

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.checkout))

        checkout = intent.getParcelableExtra(CHECKOUT)
        address = intent.getParcelableExtra(ADDRESS)

        addCardListener()
        priceView.setCheckout(checkout)
        payButton.setOnClickListener { loadData() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CARD_EDIT) {

            val name = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME) ?: ""
            val cardNumber = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER) ?: ""
            val expiry = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY) ?: ""
            val cvv = data?.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV) ?: ""

            creditCardView.setCardExpiry(expiry)
            creditCardView.cardNumber = cardNumber
            creditCardView.cardHolderName = name
            creditCardView.cvv = cvv

            val cardHolder = cardManager.splitHolderName(name)
            val cardExpireDate = cardManager.splitExpireDate(expiry)

            if (cardHolder != null && cardExpireDate != null) {
                val card = Card(cardHolder.first, cardHolder.second, cardNumber,
                        cardExpireDate.first, cardExpireDate.second, cvv)
                checkCardValidation(card)
            }
        }
    }

    //INIT

    override fun inject() {
        ShopifyWrapper.component.attachCardPaymentComponent(CardPaymentModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_card_payment

    override fun createPresenter() = cardPaymentPresenter

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        card?.let { presenter.pay(checkout, it, address) }
    }

    override fun showContent(data: Boolean) {
        super.showContent(data)
        if (data) {
            //TODO add checkout polling
        } else {
            showMessage(R.string.payment_canceled)
            finish()
        }
    }

    private fun addCardListener() {
        creditCardView.setOnClickListener {
            val intent = Intent(this@CardPaymentActivity, CardEditActivity::class.java)
            startActivityForResult(intent, CARD_EDIT)
        }
    }

    private fun checkCardValidation(creditCard: Card) {
        payButton.isEnabled = false
        card = null

        when (cardManager.isCardValid(creditCard)) {
            CardValidator.CardValidationResult.VALID -> {
                card = creditCard
                payButton.isEnabled = true
            }
            CardValidator.CardValidationResult.INVALID_NAME -> showMessage(R.string.card_name_error)
            CardValidator.CardValidationResult.INVALID_DATE -> showMessage(R.string.card_date_error)
            CardValidator.CardValidationResult.INVALID_CVV -> showMessage(R.string.card_cvv_error)
            CardValidator.CardValidationResult.INVALID_NUMBER -> showMessage(R.string.card_number_error)
        }
    }
}