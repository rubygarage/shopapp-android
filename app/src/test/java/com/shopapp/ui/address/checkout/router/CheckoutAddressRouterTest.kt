package com.shopapp.ui.address.checkout.router

import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.checkout.CheckoutAddressActivity
import com.shopapp.ui.address.checkout.CheckoutAddressListActivity
import com.shopapp.ui.const.Extra
import com.shopapp.ui.const.RequestCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CheckoutAddressRouterTest {

    private lateinit var activity: CheckoutAddressListActivity
    private lateinit var router: CheckoutAddressRouter

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(CheckoutAddressListActivity::class.java)
        router = CheckoutAddressRouter()
    }

    @Test
    fun shouldShowCheckoutAddressActivity() {
        val address = MockInstantiator.newAddress()
        router.showCheckoutAddressForResult(activity, RequestCode.ADD_BILLING_ADDRESS, address, true)

        val intentForResult = Shadows.shadowOf(activity).nextStartedActivityForResult
        assertEquals(RequestCode.ADD_BILLING_ADDRESS, intentForResult.requestCode)
        val intent = Shadows.shadowOf(intentForResult.intent)

        assertEquals(CheckoutAddressActivity::class.java, intent.intentClass)
        assertEquals(address, intentForResult.intent.extras.getParcelable(Extra.ADDRESS))
        assertTrue(intentForResult.intent.extras.getBoolean(Extra.IS_SELECTED_ADDRESS))
    }

}