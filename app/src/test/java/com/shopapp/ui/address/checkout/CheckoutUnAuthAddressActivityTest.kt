package com.shopapp.ui.address.checkout

import android.app.Activity
import android.content.Context
import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.base.BaseAddressActivity
import com.shopapp.ui.const.Extra
import kotlinx.android.synthetic.main.activity_address.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CheckoutUnAuthAddressActivityTest {

    companion object {
        private const val CHECKOUT_ID = "checkout_id"
    }

    private lateinit var context: Context
    private lateinit var activity: CheckoutUnAuthAddressActivity
    private lateinit var address: Address

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        address = MockInstantiator.newAddress()
        val intent = CheckoutUnAuthAddressActivity.getStartIntent(context, CHECKOUT_ID, address, true)
        activity = Robolectric.buildActivity(CheckoutUnAuthAddressActivity::class.java, intent).create().get()
    }

    @Test
    fun shouldExtractDataFromBundle() {
        val address: Address = activity.intent.getParcelableExtra(Extra.ADDRESS)
        assertNotNull(address)
        assertEquals(this.address, address)
        assertEquals(CHECKOUT_ID, activity.intent.getStringExtra(Extra.CHECKOUT_ID))
        assertTrue(activity.intent.getBooleanExtra(Extra.IS_SHIPPING, false))
    }

    @Test
    fun shouldSubmitAddressWhenSubmitButtonClicked() {
        activity.submitButton.performClick()

        argumentCaptor<Address>().apply {
            verify(activity.presenter).submitShippingAddress(eq(CHECKOUT_ID), capture())

            assertEquals(address.firstName, firstValue.firstName)
            assertEquals(address.lastName, firstValue.lastName)
            assertEquals(address.address, firstValue.address)
            assertEquals(address.secondAddress, firstValue.secondAddress)
            assertEquals(address.city, firstValue.city)
            assertEquals(address.state, firstValue.state)
            assertEquals(address.country, firstValue.country)
            assertEquals(address.zip.toUpperCase(), firstValue.zip)
            assertEquals(address.phone, firstValue.phone)
        }
    }

    @Test
    fun shouldShowErrorWhenSubmitButtonClicked() {
        val intent = CheckoutUnAuthAddressActivity.getStartIntent(context, CHECKOUT_ID, address, false)
        val activity = Robolectric.buildActivity(CheckoutUnAuthAddressActivity::class.java, intent).create().get()
        given(activity.fieldValidator.isAddressValid(any())).willReturn(false)
        activity.submitButton.performClick()

        assertEquals(context.getString(R.string.invalid_address), ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun shouldSetResultAndFinishWhenSubmitButtonClicked() {
        val intent = CheckoutUnAuthAddressActivity.getStartIntent(context, CHECKOUT_ID, address, false)
        val activity = Robolectric.buildActivity(CheckoutUnAuthAddressActivity::class.java, intent).create().get()
        given(activity.fieldValidator.isAddressValid(any())).willReturn(true)
        activity.submitButton.performClick()

        val shadowActivity = Shadows.shadowOf(activity)
        val resultIntent = shadowActivity.resultIntent
        val resultAddress: Address = resultIntent.extras.getParcelable(Extra.ADDRESS)

        assertEquals(address.firstName, resultAddress.firstName)
        assertEquals(address.lastName, resultAddress.lastName)
        assertEquals(address.address, resultAddress.address)
        assertEquals(address.secondAddress, resultAddress.secondAddress)
        assertEquals(address.city, resultAddress.city)
        assertEquals(address.state, resultAddress.state)
        assertEquals(address.country, resultAddress.country)
        assertEquals(address.zip.toUpperCase(), resultAddress.zip)
        assertEquals(address.phone, resultAddress.phone)

        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertTrue(resultIntent.extras.getBoolean(Extra.IS_ADDRESS_CHANGED))
        assertTrue(shadowActivity.isFinishing)
    }

    @Test
    fun shouldSetResultAndFinishWhenAddressChanged() {
        activity.addressChanged(address)
        val shadowActivity = Shadows.shadowOf(activity)
        val resultIntent = shadowActivity.resultIntent

        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertEquals(address, resultIntent.extras.getParcelable(Extra.ADDRESS))
        assertTrue(resultIntent.extras.getBoolean(Extra.IS_ADDRESS_CHANGED))
        assertTrue(shadowActivity.isFinishing)
    }
}