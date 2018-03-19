package com.shopapp.ui.address.account

import android.content.Context
import com.shopapp.TestShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.const.Extra
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = TestShopApplication::class)
class AddressActivityTest {

    private lateinit var context: Context
    private lateinit var activity: AddressActivity
    private lateinit var address: Address

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        address = MockInstantiator.newAddress()
        val intent = AddressActivity.getStartIntent(context, address)
        activity = Robolectric.buildActivity(AddressActivity::class.java, intent).create().get()
    }

    @Test
    fun shouldExtractAddressFromBundle() {
        val address: Address = activity.intent.getParcelableExtra(Extra.ADDRESS)
        assertNotNull(address)
        assertEquals(this.address, address)
    }
}