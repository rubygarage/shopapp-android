package com.shopapp.ui.checkout.payment.card

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import com.shopapp.gateway.entity.Card
import com.shopapp.gateway.entity.CardType
import com.shopapp.domain.detector.CardTypeDetector
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.ext.hideKeyboard
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.lce.view.LceLayout
import com.shopapp.ui.base.picker.BaseBottomSheetPicker
import com.shopapp.ui.checkout.payment.card.contract.CardPresenter
import com.shopapp.ui.checkout.payment.card.contract.CardView
import com.shopapp.ui.const.Extra
import com.shopapp.ui.custom.CreditCardFormatTextWatcher
import com.shopapp.ui.custom.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_card.*
import javax.inject.Inject


class CardActivity :
    BaseLceActivity<List<CardType>, CardView, CardPresenter>(),
    CardView {

    companion object {

        private const val CARD = "card"

        fun getStartIntent(context: Context, card: Card?): Intent {
            val intent = Intent(context, CardActivity::class.java)
            intent.putExtra(CARD, card)
            return intent
        }
    }

    @Inject
    lateinit var cardPresenter: CardPresenter
    private lateinit var cardMaskTextWatcher: TextWatcher
    private lateinit var cardTextWatcher: TextWatcher
    private lateinit var fieldTextWatcher: TextWatcher
    private lateinit var monthPicker: DateBottomSheetPicker
    private lateinit var yearPicker: DateBottomSheetPicker
    private var card: Card? = null
    private val cardTypeDetector: CardTypeDetector = CardTypeDetector()

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.credit_card))
        setupPickers()
        setupListeners()
        card = intent.getParcelableExtra(CARD)

        loadData()
    }

    override fun onResume() {
        super.onResume()
        if (this::cardMaskTextWatcher.isInitialized) {
            cardNumberInput.addTextChangedListener(cardMaskTextWatcher)
        }
        if (this::cardTextWatcher.isInitialized) {
            cardNumberInput.addTextChangedListener(cardTextWatcher)
        }
        if (this::fieldTextWatcher.isInitialized) {
            holderNameInput.addTextChangedListener(fieldTextWatcher)
            cardNumberInput.addTextChangedListener(fieldTextWatcher)
            monthInput.addTextChangedListener(fieldTextWatcher)
            yearInput.addTextChangedListener(fieldTextWatcher)
            cvvInput.addTextChangedListener(fieldTextWatcher)
            fillCardData()
        }
    }

    override fun onPause() {
        super.onPause()
        cardNumberInput.removeTextChangedListener(cardMaskTextWatcher)
        cardNumberInput.removeTextChangedListener(cardTextWatcher)
        holderNameInput.removeTextChangedListener(fieldTextWatcher)
        cardNumberInput.removeTextChangedListener(fieldTextWatcher)
        monthInput.removeTextChangedListener(fieldTextWatcher)
        yearInput.removeTextChangedListener(fieldTextWatcher)
        cvvInput.removeTextChangedListener(fieldTextWatcher)
    }

    //INIT

    override fun getContentView() = R.layout.activity_card

    override fun createPresenter() = cardPresenter

    override fun inject() {
        ShopApplication.appComponent.attachCardPaymentComponent().inject(this)
    }

    //SETUP

    private fun setupPickers() {
        monthPicker = DateBottomSheetPicker.newInstance(DateBottomSheetPicker.DATE_TYPE_MONTH)
        monthPicker.onDoneButtonClickedListener = object : BaseBottomSheetPicker.OnDoneButtonClickedListener<String> {
            override fun onDoneButtonClicked(selectedData: String) {
                monthInput.setText(selectedData)
            }
        }
        yearPicker = DateBottomSheetPicker.newInstance(DateBottomSheetPicker.DATE_TYPE_YEAR)
        yearPicker.onDoneButtonClickedListener = object : BaseBottomSheetPicker.OnDoneButtonClickedListener<String> {
            override fun onDoneButtonClicked(selectedData: String) {
                yearInput.setText(selectedData)
            }
        }
    }

    private fun setupListeners() {
        cardMaskTextWatcher = CreditCardFormatTextWatcher(this, resources.getDimension(R.dimen.card_padding))
        cardTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                cardLogo.setImageResource(cardTypeDetector.detect(s.toString())?.logoRes ?: 0)
            }
        }
        fieldTextWatcher = object : SimpleTextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkInputFields()
            }
        }
        monthInput.setOnClickListener {
            it.hideKeyboard()
            showMonthPicker()
        }
        yearInput.setOnClickListener {
            it.hideKeyboard()
            yearPicker.show(supportFragmentManager, DateBottomSheetPicker.DATE_TYPE_YEAR, yearInput.text.toString())
        }
        submitButton.setOnClickListener {
            changeState(LceLayout.LceState.LoadingState(true))
            presenter.processCardData(
                holderNameInput.text.toString(),
                cardNumberInput.text.toString(),
                monthInput.text.toString(),
                yearInput.text.toString(),
                cvvInput.text.toString()
            )
        }
        cardNumberInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                v.hideKeyboard()
                v.clearFocus()
                showMonthPicker()
                true
            } else {
                false
            }
        }
    }

    private fun showMonthPicker() {
        monthPicker.show(supportFragmentManager, DateBottomSheetPicker.DATE_TYPE_MONTH, monthInput.text.toString())
    }

    private fun checkInputFields() {
        submitButton.isEnabled = holderNameInput.text.isNotBlank() &&
                cardNumberInput.text.isNotBlank() &&
                monthInput.text.isNotBlank() &&
                yearInput.text.isNotBlank() &&
                cvvInput.text.isNotBlank()
    }

    private fun fillCardData() {
        if (holderNameInput.text.isBlank()) {
            card?.let {
                holderNameInput.setText(getString(R.string.full_name_pattern, it.firstName, it.lastName))
                cardNumberInput.setText(it.cardNumber)
                monthInput.setText(it.expireMonth)
                yearInput.setText(it.expireYear)
                cvvInput.setText(it.verificationCode)
            }
        }
    }

    private fun showCardLogo(cardType: CardType) {
        val cardLogo = when (cardType) {
            CardType.VISA -> visa
            CardType.MASTER_CARD -> masterCard
            CardType.AMERICAN_EXPRESS -> amex
            CardType.DISCOVER -> discover
            CardType.DINERS_CLUB -> dc
            CardType.JCB -> jcb
        }
        cardLogo.visibility = View.VISIBLE
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.getAcceptedCardTypes()
    }

    override fun showContent(data: List<CardType>) {
        super.showContent(data)
        data.map { showCardLogo(it) }
    }

    override fun cardTokenReceived(data: Pair<Card, String>) {
        changeState(LceLayout.LceState.ContentState)
        val result = Intent()
        result.putExtra(Extra.CARD, data.first)
        result.putExtra(Extra.CARD_TOKEN, data.second)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    override fun cardPassValidation(card: Card) {
        if (card == this.card) {
            onBackPressed()
        } else {
            presenter.getToken(card)
        }
    }

    override fun cardValidationError(error: Int) {
        changeState(LceLayout.LceState.ContentState)
        showMessage(error)
    }
}