package com.shopapp.ui.checkout.router

import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Checkout
import com.shopapp.gateway.entity.Customer
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.checkout.CheckoutAddressListActivity
import com.shopapp.ui.address.checkout.CheckoutUnAuthAddressActivity
import com.shopapp.ui.checkout.CheckoutActivity
import com.shopapp.ui.checkout.payment.PaymentActivity
import com.shopapp.ui.checkout.payment.card.CardActivity
import com.shopapp.ui.const.Extra
import com.shopapp.ui.const.RequestCode
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.order.success.OrderSuccessActivity
import kotlinx.android.synthetic.main.activity_checkout.*
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CheckoutRouterTest {
    private lateinit var activity: CheckoutActivity
    private lateinit var address: Address
    private lateinit var router: CheckoutRouter

    @Before
    fun setUpTest() {
        val intent = CheckoutActivity.getStartIntent(RuntimeEnvironment.application.baseContext)
        activity = Robolectric.buildActivity(CheckoutActivity::class.java, intent).create().get()
        address = MockInstantiator.newAddress()
        router = CheckoutRouter()
    }

    @Test
    fun shouldStartOrderSuccessActivityWhenCheckoutCompleted() {
        val order = MockInstantiator.newOrder()
        activity.checkoutCompleted(order)
        router.showSuccessOrder(activity, order.id, order.orderNumber)

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(order.id, startedIntent.extras.getString(OrderSuccessActivity.ORDER_ID))
        assertEquals(order.orderNumber, startedIntent.extras.getInt(OrderSuccessActivity.ORDER_NUMBER))
        assertEquals(OrderSuccessActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldShowCheckoutAddressList() {
        router.showCheckoutAddressListForResult(
            activity,
            MockInstantiator.DEFAULT_ID,
            address,
            true,
            null,
            activity.paymentView.getAddress(),
            RequestCode.EDIT_SHIPPING_ADDRESS
        )

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(
            MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(Extra.CHECKOUT_ID))
        assertEquals(address, startedIntent.extras.getParcelable(Extra.SELECTED_ADDRESS))
        assertEquals(true, startedIntent.extras.getBoolean(Extra.IS_SHIPPING))
        assertEquals(CheckoutAddressListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCheckoutUnAuthAddressActivityWhenShippingAddressViewEditButtonClicked() {
        router.showCheckoutUnAuthAddressListForResult(
            activity,
            MockInstantiator.DEFAULT_ID,
            address,
            true,
            RequestCode.EDIT_SHIPPING_ADDRESS
        )
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(Extra.CHECKOUT_ID))
        assertEquals(address, startedIntent.extras.getParcelable(Extra.ADDRESS))
        assertEquals(true, startedIntent.extras.getBoolean(Extra.IS_SHIPPING))
        assertEquals(CheckoutUnAuthAddressActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCheckoutAddressListActivityWhenShippingAddressViewAddNewAddressButtonClicked() {
        router.showCheckoutAddressListForResult(
            activity,
            MockInstantiator.DEFAULT_ID,
            address,
            true,
            null,
            activity.paymentView.getAddress(),
            RequestCode.ADD_SHIPPING_ADDRESS
        )
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(Extra.CHECKOUT_ID))
        assertEquals(address, startedIntent.extras.getParcelable(Extra.SELECTED_ADDRESS))
        assertEquals(true, startedIntent.extras.getBoolean(Extra.IS_SHIPPING))
        assertEquals(CheckoutAddressListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCheckoutUnAuthAddressActivityWhenShippingAddressViewAddNewAddressButtonClicked() {
        router.showCheckoutUnAuthAddressListForResult(
            activity,
            MockInstantiator.DEFAULT_ID,
            address,
            true,
            RequestCode.ADD_SHIPPING_ADDRESS
        )

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(MockInstantiator.DEFAULT_ID, startedIntent.extras.getString(Extra.CHECKOUT_ID))
        assertEquals(address, startedIntent.extras.getParcelable(Extra.ADDRESS))
        assertEquals(true, startedIntent.extras.getBoolean(Extra.IS_SHIPPING))
        assertEquals(CheckoutUnAuthAddressActivity::class.java, shadowIntent.intentClass)
    }


    @Test
    fun shouldStartPaymentActivityWhenPaymentViewAddPaymentTypeButtonClicked() {
        router.showPaymentResult(activity, activity.paymentView.getPaymentType(), RequestCode.PAYMENT)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(PaymentActivity::class.java, shadowIntent.intentClass)
    }


    @Test
    fun shouldStartCardActivityWhenPaymentViewAddCardButtonClicked() {
        router.showCardForResult(activity, activity.paymentView.getCardData().first, RequestCode.CARD)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(CardActivity::class.java, shadowIntent.intentClass)
    }


    @Test
    fun shouldStartHomeActivityWhenFailureViewBackToShopClicked() {
        router.showHome(activity, true)
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(HomeActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCheckoutAddressListActivityWhenPaymentViewAddAddressButtonClicked() {
        router.showCheckoutAddressListForResult(
            activity = activity,
            selectedAddress = address,
            isShippingAddress = false,
            shippingAddress = address,
            billingAddress = null,
            requestCode = RequestCode.ADD_BILLING_ADDRESS
        )

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertNull(startedIntent.extras.getString(Extra.CHECKOUT_ID))
        assertEquals(address, startedIntent.extras.getParcelable(Extra.SELECTED_ADDRESS))
        assertEquals(false, startedIntent.extras.getBoolean(Extra.IS_SHIPPING))
        assertEquals(CheckoutAddressListActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartCheckoutUnAuthAddressActivityWhenPaymentViewAddAddressButtonClicked() {
        val address = MockInstantiator.newAddress()

        router.showCheckoutUnAuthAddressListForResult(
            activity = activity,
            selectedAddress = address,
            isShippingAddress = false,
            requestCode = RequestCode.ADD_BILLING_ADDRESS
        )
        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertNull(startedIntent.extras.getString(Extra.CHECKOUT_ID))
        assertEquals(address, startedIntent.extras.getParcelable(Extra.ADDRESS))
        assertEquals(false, startedIntent.extras.getBoolean(Extra.IS_SHIPPING))
        assertEquals(CheckoutUnAuthAddressActivity::class.java, shadowIntent.intentClass)
    }
}