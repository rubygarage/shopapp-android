package com.shopapp.ui.checkout.payment

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.gateway.entity.CardType
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.checkout.payment.PaymentView.Companion.LAST_CARD_NUMBERS
import com.shopapp.ui.checkout.payment.PaymentView.Companion.LAST_DATE_NUMBERS
import com.shopapp.ui.const.PaymentType
import kotlinx.android.synthetic.main.view_payment.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class PaymentViewTest {

    @Mock
    private lateinit var paymentClickListener: View.OnClickListener

    @Mock
    private lateinit var cardClickListener: View.OnClickListener

    @Mock
    private lateinit var addAddressClickListener: View.OnClickListener

    private lateinit var context: Context
    private lateinit var view: PaymentView

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        context = RuntimeEnvironment.application.baseContext
        view = PaymentView(context)
        view.setClickListeners(paymentClickListener, cardClickListener, addAddressClickListener)
    }

    @Test
    fun shouldSetUpCardPaymentTypeCorrectly() {
        view.setPaymentType(PaymentType.CARD_PAYMENT)

        val shadowDrawable = shadowOf(view.paymentTypeText.compoundDrawables[0])
        assertEquals(R.drawable.ic_credit_card, shadowDrawable.createdFromResId)
        assertEquals(context.getString(R.string.credit_card), view.paymentTypeText.text)
        assertEquals(View.GONE, view.addPaymentTypeButton.visibility)
        assertEquals(View.VISIBLE, view.paymentGroup.visibility)

        assertEquals(View.VISIBLE, view.addCardButton.visibility)
        assertEquals(View.VISIBLE, view.addAddressButton.visibility)
        assertEquals(View.VISIBLE, view.addressBottomSpace.visibility)
        assertEquals(View.VISIBLE, view.cardTypeDivider.visibility)
    }

    @Test
    fun shouldSetUpAndroidPaymentTypeCorrectly() {
        view.setPaymentType(PaymentType.ANDROID_PAYMENT)

        val shadowDrawable = shadowOf(view.paymentTypeText.compoundDrawables[0])
        assertEquals(R.drawable.ic_android, shadowDrawable.createdFromResId)
        assertEquals(context.getString(R.string.android_payment), view.paymentTypeText.text)
        assertEquals(View.GONE, view.addPaymentTypeButton.visibility)
        assertEquals(View.VISIBLE, view.paymentGroup.visibility)
    }

    @Test
    fun shouldSetUpWebPaymentTypeCorrectly() {
        view.setPaymentType(PaymentType.WEB_PAYMENT)

        val shadowDrawable = shadowOf(view.paymentTypeText.compoundDrawables[0])
        assertEquals(R.drawable.ic_web, shadowDrawable.createdFromResId)
        assertEquals(context.getString(R.string.credit_card), view.paymentTypeText.text)
        assertEquals(View.GONE, view.addPaymentTypeButton.visibility)
        assertEquals(View.VISIBLE, view.paymentGroup.visibility)

    }

    @Test
    fun shouldSetCardDataCorrectly() {
        val card = MockInstantiator.newCard()
        given(card.cardNumber).willReturn("4242424242424242")
        view.setCardData(card, "")

        assertEquals(View.VISIBLE, view.cardGroup.visibility)
        assertEquals(ContextCompat.getDrawable(context, CardType.VISA.logoRes), view.cardLogo.drawable)
        assertEquals(
            context.getString(R.string.card_type_placeholder,
                context.getString(CardType.VISA.nameRes),
                card.cardNumber.takeLast(LAST_CARD_NUMBERS)), view.cardDataText.text
        )
        assertEquals(
            context.getString(R.string.card_exp_placeholder,
                card.expireMonth,
                card.expireYear.takeLast(LAST_DATE_NUMBERS)), view.cardExpiredDate.text)
        assertEquals(
            context.getString(R.string.full_name_pattern, card.firstName, card.lastName),
            view.cardHolder.text
        )
        assertEquals(View.GONE, view.addCardButton.visibility)
    }

    @Test
    fun shouldSetCardDataCorrectlyWithUnknownCardType() {
        val card = MockInstantiator.newCard()
        given(card.cardNumber).willReturn("0000000000000000")
        view.setCardData(card, "")

        assertEquals(View.VISIBLE, view.cardGroup.visibility)
        assertEquals(View.GONE, view.cardLogo.visibility)
        assertEquals(context.getString(
            R.string.card_unknown_placeholder,
            card.cardNumber.takeLast(LAST_CARD_NUMBERS)
        ), view.cardDataText.text)
        assertEquals(
            context.getString(R.string.card_exp_placeholder,
                card.expireMonth,
                card.expireYear.takeLast(LAST_DATE_NUMBERS)), view.cardExpiredDate.text)
        assertEquals(
            context.getString(R.string.full_name_pattern, card.firstName, card.lastName),
            view.cardHolder.text
        )
        assertEquals(View.GONE, view.addCardButton.visibility)
    }

    @Test
    fun shouldHideCardDataWhenCardIsNull() {
        view.setCardData(null, "")

        assertEquals(View.VISIBLE, view.addCardButton.visibility)
        assertEquals(View.GONE, view.cardGroup.visibility)
    }

    @Test
    fun shouldSetAddressDataCorrectly() {
        val address = MockInstantiator.newAddress()
        view.setAddressData(address)

        assertEquals(View.VISIBLE, view.addressGroup.visibility)
        assertEquals(View.GONE, view.addAddressButton.visibility)
    }

    @Test
    fun shouldHideAddressDataWhenAddressIsNull() {
        view.setAddressData(null)

        assertEquals(View.GONE, view.addressGroup.visibility)
        assertEquals(View.VISIBLE, view.addAddressButton.visibility)
    }

    @Test
    fun shouldCallOnClickWhenAddPaymentTypeButtonClicked() {
        view.addPaymentTypeButton.performClick()

        verify(paymentClickListener).onClick(view.addPaymentTypeButton)
    }

    @Test
    fun shouldCallOnClickWhenEditPaymentButton() {
        view.editPaymentButton.performClick()

        verify(paymentClickListener).onClick(view.editPaymentButton)
    }

    @Test
    fun shouldCallOnClickWhenAddCardButton() {
        view.addCardButton.performClick()

        verify(cardClickListener).onClick(view.addCardButton)
    }

    @Test
    fun shouldCallOnClickWhenEditCardButton() {
        view.editCardButton.performClick()

        verify(cardClickListener).onClick(view.editCardButton)
    }

    @Test
    fun shouldCallOnClickWhenEditAddressButton() {
        view.editAddressButton.performClick()

        verify(addAddressClickListener).onClick(view.editAddressButton)
    }

    @Test
    fun shouldCallOnClickWhenAddAddressButton() {
        view.addAddressButton.performClick()

        verify(addAddressClickListener).onClick(view.addAddressButton)
    }
}