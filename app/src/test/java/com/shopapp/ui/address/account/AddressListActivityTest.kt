package com.shopapp.ui.address.account

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.R
import com.shopapp.TestShopApplication
import com.shopapp.test.MockInstantiator
import com.shopapp.ui.address.base.BaseAddressActivity
import com.shopapp.ui.const.Extra
import com.shopapp.ui.const.RequestCode
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
        verify(activity.router).showAddressForResult(activity,  RequestCode.EDIT_SHIPPING_ADDRESS, address)

    }

    @Test
    fun shouldStartAddressActivityWhenHeaderButtonClicked() {
        activity.onClick(null)
        verify(activity.router).showAddressForResult(activity,  RequestCode.ADD_SHIPPING_ADDRESS, null)
    }

    @Test
    fun shouldReloadDataOnAddressAdded() {
        val shadowActivity = shadowOf(activity)

        activity.startActivityForResult(
            AddressActivity.getStartIntent(context),
            RequestCode.ADD_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, Intent())

        verify(activity.presenter, times(2)).getAddressList()
    }

    @Test
    fun shouldReloadDataOnAddressChanged() {
        val shadowActivity = shadowOf(activity)

        activity.startActivityForResult(
            AddressActivity.getStartIntent(context, MockInstantiator.newAddress()),
            RequestCode.EDIT_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_OK, Intent())

        verify(activity.presenter, times(2)).getAddressList()
    }

    @Test
    fun shouldNotReloadDataOnResultCancel() {
        val shadowActivity = shadowOf(activity)

        activity.startActivityForResult(
            AddressActivity.getStartIntent(context, MockInstantiator.newAddress()),
            RequestCode.EDIT_SHIPPING_ADDRESS
        )

        val requestIntent = shadowActivity.nextStartedActivityForResult
        shadowActivity.receiveResult(requestIntent.intent, Activity.RESULT_CANCELED, Intent())

        verify(activity.presenter, times(1)).getAddressList()
    }
}