package com.shopapp.ui.address.account.router

import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.account.AddressActivity
import com.shopapp.ui.address.account.AddressListActivity
import com.shopapp.ui.const.RequestCode
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class AddressListRouterTest {

    private lateinit var activity: AddressListActivity
    private lateinit var router: AddressListRouter

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(AddressListActivity::class.java)
        router = AddressListRouter()
    }

    @Test
    fun shouldShowAddressActivity() {
        val address = MockInstantiator.newAddress()
        router.showAddressForResult(activity, RequestCode.ADD_BILLING_ADDRESS, address)

        val startedIntent = Shadows.shadowOf(activity).nextStartedActivity
        val shadowIntent = Shadows.shadowOf(startedIntent)
        assertEquals(AddressActivity::class.java, shadowIntent.intentClass)
    }

}