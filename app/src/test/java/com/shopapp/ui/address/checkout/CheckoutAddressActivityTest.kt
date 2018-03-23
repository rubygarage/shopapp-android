package com.shopapp.ui.address.checkout

import android.app.Activity
import android.content.Context
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.Extra
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class CheckoutAddressActivityTest {

    private lateinit var context: Context
    private lateinit var activity: CheckoutAddressActivity
    private lateinit var address: Address

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        address = MockInstantiator.newAddress()
        val intent = CheckoutAddressActivity.getStartIntent(context, address, true)
        activity = Robolectric.buildActivity(CheckoutAddressActivity::class.java, intent).create().get()
    }

    @Test
    fun shouldExtractDataFromBundle() {
        val address: Address = activity.intent.getParcelableExtra(Extra.ADDRESS)
        assertNotNull(address)
        assertEquals(this.address, address)
        assertTrue(activity.intent.getBooleanExtra(Extra.IS_SELECTED_ADDRESS, false))
    }

    @Test
    fun shouldSetResultAndFinishWhenAddressChanged() {
        activity.addressChanged(address)
        val shadowActivity = shadowOf(activity)
        val resultIntent = shadowActivity.resultIntent

        assertEquals(Activity.RESULT_OK, shadowActivity.resultCode)
        assertEquals(address, resultIntent.extras.getParcelable(Extra.ADDRESS))
        assertTrue(resultIntent.extras.getBoolean(Extra.IS_SELECTED_ADDRESS))
        assertTrue(shadowActivity.isFinishing)
    }
}