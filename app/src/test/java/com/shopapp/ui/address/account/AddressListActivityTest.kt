package com.shopapp.ui.address.account

import android.content.Context
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.base.BaseAddressActivity
import kotlinx.android.synthetic.main.activity_address_list.*
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.view_base_toolbar.view.*
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
class AddressListActivityTest {

    private lateinit var context: Context
    private lateinit var activity: AddressListActivity

    @Before
    fun setUpTest() {
        context = RuntimeEnvironment.application.baseContext
        activity = Robolectric.buildActivity(AddressListActivity::class.java).create().get()
    }

    @Test
    fun shouldSetCorrectTitle() {
        assertEquals(context.getString(R.string.shipping_address), activity.toolbar.toolbarTitle.text)
    }

    @Test
    fun shouldSetNotNullAdapter() {
        assertNotNull(activity.recyclerView.adapter)
    }

    @Test
    fun shouldStartAddressActivityWhenEditButtonClicked() {
        val address = MockInstantiator.newAddress()
        activity.onEditButtonClicked(address)

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertEquals(address, startedIntent.extras.getParcelable(BaseAddressActivity.ADDRESS))
        assertEquals(AddressActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldStartAddressActivityWhenHeaderButtonClicked() {
        activity.onClick(null)

        val startedIntent = shadowOf(activity).nextStartedActivity
        val shadowIntent = shadowOf(startedIntent)
        assertNull(startedIntent.extras.getParcelable(BaseAddressActivity.ADDRESS))
        assertEquals(AddressActivity::class.java, shadowIntent.intentClass)
    }
}