package com.shopapp.ui.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Checkout
import com.shopapp.gateway.entity.Customer
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.checkout.CheckoutAddressListActivity
import com.shopapp.ui.address.checkout.CheckoutUnAuthAddressActivity
import com.shopapp.ui.checkout.payment.PaymentActivity
import com.shopapp.ui.checkout.payment.card.CardActivity
import com.shopapp.ui.const.Extra
import com.shopapp.ui.const.PaymentType
import com.shopapp.ui.const.RequestCode
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import kotlinx.android.synthetic.main.view_checkout_failure.view.*
import kotlinx.android.synthetic.main.view_checkout_my_cart.view.*
import kotlinx.android.synthetic.main.view_payment.view.*
import kotlinx.android.synthetic.main.view_shipping_address.view.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CheckoutActivityTest {

    private lateinit var context: Context
    private lateinit var activity: CheckoutActivity
    private lateinit var checkout: Checkout
    private lateinit var address: Address
    private lateinit var customer: Customer

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        val intent = CheckoutActivity.getStartIntent(context)
        activity = Robolectric.buildActivity(CheckoutActivity::class.java, intent).create().get()
        checkout = MockInstantiator.newCheckout()
        address = MockInstantiator.newAddress()
        customer = MockInstantiator.newCustomer()
    }

    @Test
    fun shouldSetTitleOnCreate() {
        assertEquals(context.getString(R.string.checkout), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldLoadCheckoutDataWhenOnCreate() {
        verify(activity.presenter).getCheckoutData()
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldLoadCheckoutWhenCheckoutIsNotNull() {
        activity.showContent(checkout)
        activity.loadData()
        verify(activity.presenter).getCheckout(checkout.checkoutId)
        assertEquals(View.VISIBLE, activity.lceLayout.loadingView.visibility)
    }

    @Test
    fun shouldRequestShippingRatesWhenAddressIsNotNull() {
        activity.showContent(checkout)
        verify(activity.presenter).getShippingRates(checkout.checkoutId)
    }

    @Test
    fun shouldNotRequestShippingRatesWhenAddressIsNull() {
        given(checkout.address).willReturn(null)
        activity.showContent(checkout)
        verify(activity.presenter, never()).getShippingRates(checkout.checkoutId)
    }

    @Test
    fun shouldVerifyCheckoutWhenCheckoutLoaded() {
        activity.showContent(checkout)
        val address = checkout.address

        verify(activity.presenter).verifyCheckoutData(eq(checkout), eq(address), anyOrNull(),
            anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
    }

    @Test
    fun shouldSetEmailWhenCustomerReceived() {
        activity.customerReceived(customer)

        val email = activity.checkoutEmailView.getEmail()
        assertNotNull(email)
        assertEquals(customer.email, email)
    }

    @Test
    fun shouldSetProductListDataWhenProductListReceived() {
        val size = 5
        val list = MockInstantiator.newList(MockInstantiator.newCartProduct(), size)
        activity.cartProductListReceived(list)

        assertEquals(size, activity.myCartView.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldSetShippingRateListDataWhenShippingRateListReceived() {
        val size = 5
        val list = MockInstantiator.newList(MockInstantiator.newShippingRate(), size)
        activity.showContent(checkout)
        activity.shippingRatesReceived(list)

        assertEquals(size, activity.shippingOptionsView.recyclerView.adapter.itemCount)
    }

    @Test
    fun shouldPlaceOrderButtonBeEnabledWhenCheckoutValidationPassedSuccessfully() {
        activity.checkoutValidationPassed(true)

        assertEquals(true, activity.placeOrderButton.isEnabled)
    }

    @Test
    fun shouldPlaceOrderButtonBeDisabledWhenCheckoutValidationPassedUnSuccessfully() {
        activity.checkoutValidationPassed(false)

        assertEquals(false, activity.placeOrderButton.isEnabled)
    }

    @Test
    fun shouldShowOrderSuccess() {
        val order = MockInstantiator.newOrder()
        activity.checkoutCompleted(order)
        verify(activity.router).showSuccessOrder(activity, order.id, order.orderNumber)
    }

    @Test
    fun shouldShowCheckoutErrorView() {
        activity.checkoutError()

        assertEquals(View.GONE, activity.placeOrderButton.visibility)
        assertEquals(View.VISIBLE, activity.failureView.visibility)
    }

    @Test
    fun shouldSetShippingRateWhenShippingRateSelected() {
        val shippingRate = MockInstantiator.newShippingRate()
        activity.showContent(checkout)
        activity.onOptionSelected(shippingRate)

        verify(activity.checkoutPresenter).setShippingRates(checkout, shippingRate)
    }

    @Test
    fun shouldVerifyCheckoutWhenEmailChanged() {
        activity.onEmailChanged()

        verify(activity.presenter).verifyCheckoutData(anyOrNull(), anyOrNull(), anyOrNull(),
            anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
    }

    @Test
    fun shouldShowCheckoutAddressListForEdit() {
        activity.showContent(checkout)
        activity.customerReceived(customer)
        activity.shippingAddressView.editButton.performClick()
        verify(activity.router).showCheckoutAddressListForResult(
            activity,
            checkout.checkoutId,
            checkout.address,
            true,
            null,
            activity.paymentView.getAddress(),
            RequestCode.EDIT_SHIPPING_ADDRESS
        )
    }

    @Test
    fun shouldShowCheckoutUnAuthAddressListForEdit() {
        activity.showContent(checkout)
        activity.shippingAddressView.editButton.performClick()
        verify(activity.router).showCheckoutUnAuthAddressListForResult(
            activity,
            checkout.checkoutId,
            checkout.address,
            true,
            RequestCode.EDIT_SHIPPING_ADDRESS
        )
    }

    @Test
    fun shouldShowCheckoutAddressListForAdd() {
        activity.showContent(checkout)
        activity.customerReceived(customer)
        activity.shippingAddressView.setAddress(checkout.address)
        activity.shippingAddressView.addNewAddressButton.performClick()

        verify(activity.router).showCheckoutAddressListForResult(
            activity,
            checkout.checkoutId,
            checkout.address,
            true,
            null,
            activity.paymentView.getAddress(),
            RequestCode.ADD_SHIPPING_ADDRESS
        )
    }

    @Test
    fun shouldStartCheckoutUnAuthAddressListForAdd() {
        activity.showContent(checkout)
        activity.shippingAddressView.setAddress(checkout.address)
        activity.shippingAddressView.addNewAddressButton.performClick()

        verify(activity.router).showCheckoutUnAuthAddressListForResult(
            activity,
            checkout.checkoutId,
            null,
            true,
            RequestCode.ADD_SHIPPING_ADDRESS
        )
    }

    @Test
    fun shouldShowPayment() {
        activity.paymentView.addPaymentTypeButton.performClick()
        verify(activity.router).showPaymentResult(
            activity,
            activity.paymentView.getPaymentType(),
            RequestCode.PAYMENT
        )
    }

    @Test
    fun shouldShowCard() {
        activity.paymentView.addCardButton.performClick()
        verify(activity.router).showCardForResult(
            activity,
            activity.paymentView.getCardData().first,
            RequestCode.CARD
        )
    }

    @Test
    fun shouldShowCheckoutAddressListForAddWhenPaymentViewAddAddressButtonClicked() {
        activity.showContent(checkout)
        activity.customerReceived(customer)
        activity.paymentView.setAddressData(checkout.address)
        activity.paymentView.addAddressButton.performClick()


        verify(activity.router).showCheckoutAddressListForResult(
            activity = activity,
            selectedAddress = activity.paymentView.getAddress(),
            isShippingAddress = false,
            shippingAddress = checkout.address,
            billingAddress = null,
            requestCode = RequestCode.ADD_BILLING_ADDRESS
        )
    }

    @Test
    fun shouldShowCheckoutUnAuthAddressForAddWhenPaymentViewAddAddressButtonClicked() {
        activity.paymentView.setAddressData(checkout.address)
        activity.paymentView.addAddressButton.performClick()

        verify(activity.router).showCheckoutUnAuthAddressListForResult(
            activity = activity,
            selectedAddress = activity.paymentView.getAddress(),
            isShippingAddress = false,
            requestCode = RequestCode.ADD_BILLING_ADDRESS
        )
    }

    @Test
    fun shouldShowHome() {
        activity.failureView.backToShop.performClick()
        verify(activity.router).showHome(activity, true)
    }

    @Test
    fun shouldCompleteOrderByCardWhenTryAgainButtonClicked() {
        activity.paymentView.setPaymentType(PaymentType.CARD_PAYMENT)
        activity.failureView.tryAgainCheckoutButton.performClick()

        verify(activity.presenter).completeCheckoutByCard(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
    }

    @Test
    fun shouldCompleteOrderByCardWhenPlaceOrderButtonClicked() {
        activity.paymentView.setPaymentType(PaymentType.CARD_PAYMENT)
        activity.placeOrderButton.performClick()

        verify(activity.presenter).completeCheckoutByCard(anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
    }

    @Test
    fun shouldNotReloadCheckoutIfShippingAddressNotChanged() {
        val shadowActivity = shadowOf(activity)
        val dataIntent = Intent()
        dataIntent.putExtra(Extra.IS_ADDRESS_CHANGED, false)

        activity.showContent(checkout)
        activity.startActivityForResult(
            CheckoutUnAuthAddressActivity.getStartIntent(context, checkout.checkoutId, isShipping = true),
            RequestCode.ADD_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, dataIntent)

        verify(activity.presenter, times(1)).getCheckoutData()
        verify(activity.presenter, atLeastOnce()).verifyCheckoutData(any(), any(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
    }

    @Test
    fun shouldReloadCheckoutDataOnShippingAddressAdded() {
        val shadowActivity = shadowOf(activity)
        val dataIntent = Intent()
        dataIntent.putExtra(Extra.IS_ADDRESS_CHANGED, true)

        activity.showContent(checkout)
        activity.startActivityForResult(
            CheckoutUnAuthAddressActivity.getStartIntent(context, checkout.checkoutId, isShipping = true),
            RequestCode.ADD_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, dataIntent)

        verify(activity.presenter).getCheckoutData()
        verify(activity.presenter, atLeastOnce()).verifyCheckoutData(any(), any(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
    }

    @Test
    fun shouldReloadCheckoutDataOnShippingAddressEdited() {
        val shadowActivity = shadowOf(activity)
        val dataIntent = Intent()
        dataIntent.putExtra(Extra.IS_ADDRESS_CHANGED, true)

        activity.showContent(checkout)
        activity.startActivityForResult(
            CheckoutUnAuthAddressActivity.getStartIntent(context, checkout.checkoutId, isShipping = true),
            RequestCode.EDIT_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, dataIntent)

        verify(activity.presenter).getCheckoutData()
        verify(activity.presenter, atLeastOnce()).verifyCheckoutData(any(), any(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull(), anyOrNull())
    }

    @Test
    fun shouldNotSetUpPaymentViewIfBillingAddressNotChanged() {
        val shadowActivity = shadowOf(activity)
        val dataIntent = Intent()
        dataIntent.putExtra(Extra.IS_ADDRESS_CHANGED, false)
        activity.startActivityForResult(
            CheckoutUnAuthAddressActivity.getStartIntent(context, checkout.checkoutId),
            RequestCode.ADD_BILLING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, dataIntent)
        assertEquals(null, activity.paymentView.getAddress())
    }

    @Test
    fun shouldSetUpPaymentView() {
        val shadowActivity = shadowOf(activity)
        val dataIntent = Intent()
        dataIntent.putExtra(Extra.PAYMENT_TYPE, PaymentType.CARD_PAYMENT)

        activity.startActivityForResult(
            PaymentActivity.getStartIntent(context, null),
            RequestCode.PAYMENT
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, dataIntent)
        assertEquals(PaymentType.CARD_PAYMENT, activity.paymentView.getPaymentType())
    }

    @Test
    fun shouldSetUpPaymentViewOnBillingAddressAdded() {
        val shadowActivity = shadowOf(activity)
        val dataIntent = Intent()
        dataIntent.putExtra(Extra.IS_ADDRESS_CHANGED, true)
        dataIntent.putExtra(Extra.ADDRESS, address)

        activity.startActivityForResult(
            CheckoutUnAuthAddressActivity.getStartIntent(context, checkout.checkoutId),
            RequestCode.ADD_BILLING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, dataIntent)

        verify(activity.presenter).getCheckoutData()
        assertEquals(address, activity.paymentView.getAddress())
    }

    @Test
    fun shouldSetUpPaymentViewOnBillingAddressEdited() {
        val shadowActivity = shadowOf(activity)
        val dataIntent = Intent()
        dataIntent.putExtra(Extra.IS_ADDRESS_CHANGED, true)
        dataIntent.putExtra(Extra.ADDRESS, address)

        activity.startActivityForResult(
            CheckoutUnAuthAddressActivity.getStartIntent(context, checkout.checkoutId),
            RequestCode.EDIT_BILLING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, dataIntent)

        verify(activity.presenter).getCheckoutData()
        assertEquals(address, activity.paymentView.getAddress())
    }

    @Test
    fun shouldSetUpPaymentViewOnCardChanged() {
        val card = MockInstantiator.newCard()
        val cardToken = "token"
        val shadowActivity = shadowOf(activity)
        val dataIntent = Intent()
        dataIntent.putExtra(Extra.IS_ADDRESS_CHANGED, true)
        dataIntent.putExtra(Extra.CARD, card)
        dataIntent.putExtra(Extra.CARD_TOKEN, cardToken)

        activity.startActivityForResult(CardActivity.getStartIntent(context, card), RequestCode.CARD)

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, dataIntent)

        verify(activity.presenter).getCheckoutData()
        assertEquals(card, activity.paymentView.getCardData().first)
        assertEquals(cardToken, activity.paymentView.getCardData().second)
    }

    @Test
    fun shouldVerifyDataOnCanceledResult() {
        val shadowActivity = shadowOf(activity)

        activity.showContent(checkout)
        activity.startActivityForResult(
            CheckoutUnAuthAddressActivity.getStartIntent(context, checkout.checkoutId, isShipping = true),
            RequestCode.ADD_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_CANCELED, null)
        verify(activity.presenter).getCheckoutData()
    }

    @Test
    fun shouldClearPaymentView() {
        val shadowActivity = shadowOf(activity)
        val dataIntent = Intent()
        dataIntent.putExtra(Extra.CLEAR_BILLING, true)

        activity.paymentView.setAddressData(address)

        activity.startActivityForResult(
            CheckoutAddressListActivity.getStartIntent(context, checkout.checkoutId, null, false, null, null),
            RequestCode.EDIT_BILLING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, dataIntent)

        verify(activity.presenter).getCheckoutData()
        assertEquals(null, activity.paymentView.getAddress())
    }

    @Test
    fun shouldClearShippingViews() {
        val shadowActivity = shadowOf(activity)
        val dataIntent = Intent()
        dataIntent.putExtra(Extra.CLEAR_SHIPPING, true)

        activity.shippingAddressView.setAddress(address)

        activity.startActivityForResult(
            CheckoutAddressListActivity.getStartIntent(context, checkout.checkoutId, null, true, address, null),
            RequestCode.EDIT_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, dataIntent)

        verify(activity.presenter).getCheckoutData()
        assertEquals(null, activity.shippingAddressView.getAddress())
        val messageView = activity.shippingOptionsView.findViewById<TextView>(R.id.message)
        assertEquals(View.VISIBLE, messageView.visibility)
        assertEquals(context.resources.getString(R.string.please_add_shipping_address_first), messageView.text.toString())
    }
}