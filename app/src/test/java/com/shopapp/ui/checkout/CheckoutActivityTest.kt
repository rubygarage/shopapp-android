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
import com.shopapp.ui.address.base.BaseAddressActivity
import com.shopapp.ui.address.checkout.CheckoutAddressListActivity
import com.shopapp.ui.address.checkout.CheckoutUnAuthAddressActivity
import com.shopapp.ui.checkout.payment.PaymentActivity
import com.shopapp.ui.checkout.payment.card.CardActivity
import com.shopapp.ui.const.Extra
import com.shopapp.ui.const.PaymentType
import com.shopapp.ui.const.RequestCode
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.order.success.OrderSuccessActivity
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
import kotlinx.android.synthetic.main.view_checkout_failure.view.*
import kotlinx.android.synthetic.main.view_checkout_my_cart.view.*
import kotlinx.android.synthetic.main.view_payment.view.*
import kotlinx.android.synthetic.main.view_shipping_address.view.*
import org.junit.Assert.*
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
    fun shouldStartOrderSuccessActivityWhenCheckoutCompleted() {
        val order = MockInstantiator.newOrder()
        activity.checkoutCompleted(order)

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(order.id, startedIntent.extras.getString(OrderSuccessActivity.ORDER_ID))
        assertEquals(order.orderNumber, startedIntent.extras.getInt(OrderSuccessActivity.ORDER_NUMBER))
        assertEquals(OrderSuccessActivity::class.java, shadowIntent.intentClass)
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
    fun shouldStartCheckoutAddressListActivityWhenShippingAddressViewEditButtonClicked() {
        activity.showContent(checkout)
        activity.customerReceived(customer)
        activity.shippingAddressView.editButton.performClick()

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(checkout.checkoutId, startedIntent.extras.getString(CheckoutAddressListActivity.CHECKOUT_ID))
        assertEquals(checkout.address, startedIntent.extras.getParcelable(CheckoutAddressListActivity.SELECTED_ADDRESS))
        assertEquals(true, startedIntent.extras.getBoolean(CheckoutAddressListActivity.IS_SHIPPING))
        assertEquals(CheckoutAddressListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCheckoutUnAuthAddressActivityWhenShippingAddressViewEditButtonClicked() {
        activity.showContent(checkout)
        activity.shippingAddressView.editButton.performClick()

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(checkout.checkoutId, startedIntent.extras.getString(CheckoutUnAuthAddressActivity.CHECKOUT_ID))
        assertEquals(checkout.address, startedIntent.extras.getParcelable(BaseAddressActivity.ADDRESS))
        assertEquals(true, startedIntent.extras.getBoolean(CheckoutUnAuthAddressActivity.IS_SHIPPING))
        assertEquals(CheckoutUnAuthAddressActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCheckoutAddressListActivityWhenShippingAddressViewAddNewAddressButtonClicked() {
        activity.showContent(checkout)
        activity.customerReceived(customer)
        activity.shippingAddressView.setAddress(checkout.address)
        activity.shippingAddressView.addNewAddressButton.performClick()

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(checkout.checkoutId, startedIntent.extras.getString(CheckoutAddressListActivity.CHECKOUT_ID))
        assertEquals(checkout.address, startedIntent.extras.getParcelable(CheckoutAddressListActivity.SELECTED_ADDRESS))
        assertEquals(true, startedIntent.extras.getBoolean(CheckoutAddressListActivity.IS_SHIPPING))
        assertEquals(CheckoutAddressListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCheckoutUnAuthAddressActivityWhenShippingAddressViewAddNewAddressButtonClicked() {
        activity.showContent(checkout)
        activity.shippingAddressView.setAddress(checkout.address)
        activity.shippingAddressView.addNewAddressButton.performClick()

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(checkout.checkoutId, startedIntent.extras.getString(CheckoutUnAuthAddressActivity.CHECKOUT_ID))
        assertNull(startedIntent.extras.getParcelable(BaseAddressActivity.ADDRESS))
        assertEquals(true, startedIntent.extras.getBoolean(CheckoutUnAuthAddressActivity.IS_SHIPPING))
        assertEquals(CheckoutUnAuthAddressActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartPaymentActivityWhenPaymentViewAddPaymentTypeButtonClicked() {
        activity.paymentView.addPaymentTypeButton.performClick()

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(PaymentActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCardActivityWhenPaymentViewAddCardButtonClicked() {
        activity.paymentView.addCardButton.performClick()

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(CardActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCheckoutAddressListActivityWhenPaymentViewAddAddressButtonClicked() {
        activity.customerReceived(customer)
        activity.paymentView.setAddressData(checkout.address)
        activity.paymentView.addAddressButton.performClick()

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertNull(startedIntent.extras.getString(CheckoutAddressListActivity.CHECKOUT_ID))
        assertEquals(checkout.address, startedIntent.extras.getParcelable(CheckoutAddressListActivity.SELECTED_ADDRESS))
        assertEquals(false, startedIntent.extras.getBoolean(CheckoutAddressListActivity.IS_SHIPPING))
        assertEquals(CheckoutAddressListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCheckoutUnAuthAddressActivityWhenPaymentViewAddAddressButtonClicked() {
        activity.paymentView.setAddressData(checkout.address)
        activity.paymentView.addAddressButton.performClick()

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertNull(startedIntent.extras.getString(CheckoutUnAuthAddressActivity.CHECKOUT_ID))
        assertEquals(checkout.address, startedIntent.extras.getParcelable(BaseAddressActivity.ADDRESS))
        assertEquals(false, startedIntent.extras.getBoolean(CheckoutUnAuthAddressActivity.IS_SHIPPING))
        assertEquals(CheckoutUnAuthAddressActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartHomeActivityWhenFailureViewBackToShopClicked() {
        activity.failureView.backToShop.performClick()

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(HomeActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldCompleteOrderByCardWhenTryAgainButtonClicked() {
        activity.paymentView.setPaymentType(PaymentType.CARD_PAYMENT)
        activity.failureView.tryAgainButton.performClick()

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

        verify(activity.presenter, never()).getCheckoutData()
        assertEquals(null, activity.paymentView.getAddress())
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